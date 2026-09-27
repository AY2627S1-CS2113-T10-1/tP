import assert from 'node:assert/strict';
import { readFile } from 'node:fs/promises';
import test from 'node:test';
import { ConsoleProcess, runScenario, validateFixtures } from '../.agents/skills/test-ui/scripts/run-ui-tests.mjs';

const echoProgram = `
const readline = require('node:readline');
const fs = require('node:fs');
const rl = readline.createInterface({ input: process.stdin });
process.stdout.write('scheduleflow> ');
rl.on('line', line => {
    if (line === 'exit') { console.log('Bye'); process.exit(0); }
    if (line === 'write') fs.writeFileSync('saved.txt', 'saved');
    const reply = line === 'read' ? fs.readFileSync('saved.txt', 'utf8') : line;
    process.stdout.write(reply + '\\r\\nscheduleflow> ');
});
rl.on('close', () => { console.log('Bye'); });
`;

function scenario(steps) {
    return { id: 'RUNNER-TEST', aim: 'Harness mechanics only, not application acceptance',
        setup: 'Temporary fixture process', status: 'active', sessions: [{ startup: '', steps }] };
}

test('maintained fixture schema and local skill frontmatter are valid', async () => {
    validateFixtures(JSON.parse(await readFile(new URL('ui-scenarios.json', import.meta.url), 'utf8')));
    for (const name of ['test-ui', 'seedu-java-coding-standard', 'seedu-git-standard']) {
        const skill = await readFile(new URL(`../.agents/skills/${name}/SKILL.md`, import.meta.url), 'utf8');
        assert.match(skill.replaceAll('\r\n', '\n'), new RegExp(`^---\nname: ${name}\ndescription: [^\n]+\n---\n`));
        assert.doesNotMatch(skill, /^\[TODO:/m);
    }
});

test('commands are checked in sequence and CRLF is normalized', async () => {
    const transcript = [];
    await runScenario(scenario([
        { input: 'first', output: 'first' },
        { input: 'second', output: 'second' },
        { input: 'exit', output: 'Bye', exitCode: 0 }
    ]), process.execPath, ['-e', echoProgram], line => transcript.push(line));
    const firstOutput = transcript.findIndex(line => line.startsWith('STDOUT "first'));
    assert.ok(firstOutput > 0);
    assert.ok(firstOutput < transcript.indexOf('INPUT "second"'));
    assert.ok(transcript.includes('PASSED RUNNER-TEST'));
});

test('first mismatch prevents later commands and includes diagnostics', async () => {
    const transcript = [];
    await assert.rejects(runScenario(scenario([
        { input: 'actual', output: 'wrong' },
        { input: 'exit', output: 'Bye', exitCode: 0 }
    ]), process.execPath, ['-e', echoProgram], line => transcript.push(line)),
    /FAILED RUNNER-TEST session 1 command 1[\s\S]*EXPECTED[\s\S]*ACTUAL[\s\S]*STDERR/);
    assert.ok(!transcript.includes('INPUT "exit"'));
});

test('restart retains only the scenario data and EOF is supported', async () => {
    const fixture = scenario([{ input: 'write', output: 'write' },
        { input: 'exit', output: 'Bye', exitCode: 0 }]);
    fixture.sessions.push({ startup: '', steps: [{ input: 'read', output: 'saved' },
        { input: null, output: 'Bye', exitCode: 0 }] });
    await runScenario(fixture, process.execPath, ['-e', echoProgram], () => {});
    await assert.rejects(runScenario(scenario([{ input: 'read', output: 'saved' },
        { input: 'exit', output: 'Bye', exitCode: 0 }]),
    process.execPath, ['-e', echoProgram], () => {}), /FAILED/);
});

test('startup failure can assert stderr-free nonzero exit', async () => {
    const fixture = scenario([]);
    fixture.sessions = [{ startup: 'Error: bad data\n', steps: [], exitCode: 1 }];
    await runScenario(fixture, process.execPath,
        ['-e', 'console.log("Error: bad data"); process.exit(1);'], () => {});
});

test('a hanging child times out and is terminated', async () => {
    const child = new ConsoleProcess(process.execPath, ['-e', 'setInterval(() => {}, 1000)'],
        process.cwd(), () => {});
    try {
        await assert.rejects(child.waitUntil(() => child.closed, 100), /Timed out/);
    } finally {
        await child.stop();
    }
    assert.equal(child.closed, true);
});

test('setup cannot escape the isolated directory', async () => {
    const fixture = scenario([{ input: 'exit', output: 'Bye', exitCode: 0 }]);
    fixture.files = { '../escape.txt': 'bad' };
    await assert.rejects(runScenario(fixture, process.execPath, ['-e', echoProgram], () => {}),
        /Setup path escapes/);
});

test('split CRLF chunks are compared only after the newline arrives', async () => {
    const program = `
        process.stdout.write('scheduleflow> ');
        process.stdin.once('data', () => {
            process.stdout.write('Bye\\r');
            setTimeout(() => { process.stdout.write('\\n'); process.exit(0); }, 80);
        });
    `;
    await runScenario(scenario([{ input: 'exit', output: 'Bye', exitCode: 0 }]),
        process.execPath, ['-e', program], () => {});
});

test('prompt-only response needs no invented newline', async () => {
    const program = `
        const readline = require('node:readline');
        const rl = readline.createInterface({ input: process.stdin });
        process.stdout.write('scheduleflow> ');
        rl.on('line', line => {
            if (!line) process.stdout.write('scheduleflow> ');
            else { console.log('Bye'); process.exit(0); }
        });
    `;
    await runScenario(scenario([{ input: '', output: '', newline: false },
        { input: 'exit', output: 'Bye', exitCode: 0 }]), process.execPath, ['-e', program], () => {});
});

test('stderr and unexpected exit fail the current command', async () => {
    for (const program of [
        "process.stdout.write('scheduleflow> '); process.stdin.once('data', () => { console.error('oops'); });",
        "process.stdout.write('scheduleflow> '); process.stdin.once('data', () => process.exit(3));"
    ]) {
        await assert.rejects(runScenario(scenario([{ input: 'exit', output: 'Bye', exitCode: 0 }]),
            process.execPath, ['-e', program], () => {}), /FAILED RUNNER-TEST session 1 command 1/);
    }
});
