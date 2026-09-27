#!/usr/bin/env node
import { spawn } from 'node:child_process';
import { once } from 'node:events';
import { mkdir, mkdtemp, readFile, readdir, rm, writeFile } from 'node:fs/promises';
import { tmpdir } from 'node:os';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const ROOT = fileURLToPath(new URL('../../../../', import.meta.url));
const PROMPT = 'scheduleflow> ';
const TIMEOUT_MS = 5000;
const normalize = value => value.replaceAll('\r\n', '\n');

/** Captures a single child process without losing output between commands. */
export class ConsoleProcess {
    constructor(command, args, cwd, record) {
        this.stdout = '';
        this.stderr = '';
        this.offset = 0;
        this.closed = false;
        this.code = null;
        this.error = null;
        this.record = record;
        this.child = spawn(command, args, { cwd, windowsHide: true, stdio: 'pipe' });
        this.child.stdout.setEncoding('utf8');
        this.child.stderr.setEncoding('utf8');
        this.child.stdout.on('data', text => { this.stdout += text; });
        this.child.stderr.on('data', text => { this.stderr += text; });
        this.child.on('error', error => { this.error = error; });
        // An early process exit can close stdin while a command is being sent.
        this.child.stdin.on('error', error => { this.error = error; });
        this.child.on('close', (code, signal) => {
            this.closed = true;
            this.code = code;
            this.signal = signal;
        });
    }

    async waitUntil(predicate, timeoutMs = TIMEOUT_MS) {
        const deadline = Date.now() + timeoutMs;
        while (!predicate()) {
            if (this.error) throw this.error;
            if (Date.now() >= deadline) throw new Error(`Timed out after ${timeoutMs} ms`);
            await new Promise(resolve => setTimeout(resolve, 10));
        }
    }

    async frame(expected, exitCode = null) {
        const target = exitCode === null ? expected + PROMPT : expected;
        try {
            await this.waitUntil(() => {
                const raw = this.stdout.slice(this.offset);
                // Pipes may split CRLF across chunks. Defer that trailing CR until its next byte.
                const hasPendingCr = !this.closed && raw.endsWith('\r');
                const actual = normalize(hasPendingCr ? raw.slice(0, -1) : raw);
                return this.closed || this.stderr.length > 0
                    || !target.startsWith(actual)
                    || (exitCode === null && actual === target && !hasPendingCr);
            });
        } catch (error) {
            throw this.mismatch(target, error.message);
        }
        const actual = normalize(this.stdout.slice(this.offset));
        this.offset = this.stdout.length;
        this.record(`STDOUT ${JSON.stringify(actual)}`);
        if (actual !== target || this.stderr || this.error
                || (exitCode === null ? this.closed : this.code !== exitCode)) {
            throw this.mismatch(target, 'Output, stderr or exit mismatch', actual);
        }
    }

    mismatch(expected, reason, actual = normalize(this.stdout.slice(this.offset))) {
        return new Error(`${reason}\nEXPECTED ${JSON.stringify(expected)}\n`
            + `ACTUAL ${JSON.stringify(actual)}\nSTDERR ${JSON.stringify(this.stderr)}\n`
            + `EXIT ${this.closed ? this.code : 'running'}`);
    }

    send(input) {
        this.record(`INPUT ${input === null ? '<EOF>' : JSON.stringify(input)}`);
        if (input === null) this.child.stdin.end();
        else this.child.stdin.write(input + '\n');
    }

    async stop() {
        if (!this.closed) {
            this.child.kill();
            await Promise.race([once(this.child, 'close'),
                new Promise(resolve => setTimeout(resolve, 1000))]);
            if (!this.closed) this.child.kill('SIGKILL');
        }
        this.record(`COMPLETE STDOUT ${JSON.stringify(this.stdout)}`);
        this.record(`STDERR ${JSON.stringify(this.stderr)}`);
        this.record(`EXIT ${this.code} SIGNAL ${this.signal ?? 'none'}`);
    }
}

/** Rejects malformed fixtures before launching an application. */
export function validateFixtures(fixtures) {
    if (fixtures.version !== 1 || !Array.isArray(fixtures.cases) || !fixtures.cases.length) {
        throw new Error('Fixtures require version 1 and a nonempty cases list');
    }
    const ids = new Set();
    for (const scenario of fixtures.cases) {
        if (!scenario.id || ids.has(scenario.id) || !scenario.aim || !scenario.setup
                || !['active', 'planned'].includes(scenario.status)
                || !Array.isArray(scenario.sessions) || !scenario.sessions.length) {
            throw new Error(`Invalid case: ${scenario.id}`);
        }
        ids.add(scenario.id);
        for (const session of scenario.sessions) {
            if (typeof session.startup !== 'string' || !Array.isArray(session.steps)) {
                throw new Error(`Invalid session: ${scenario.id}`);
            }
            const steps = session.steps;
            if (session.exitCode !== undefined) {
                if (!Number.isInteger(session.exitCode) || steps.length) {
                    throw new Error('Startup exit requires an integer code and no commands');
                }
                continue;
            }
            if (!steps.length || !Number.isInteger(steps.at(-1).exitCode)) {
                throw new Error(`Session must specify its final exit: ${scenario.id}`);
            }
            for (const [index, step] of steps.entries()) {
                if ((step.input !== null && (typeof step.input !== 'string' || /[\r\n]/.test(step.input)))
                        || typeof step.output !== 'string'
                        || (step.newline !== undefined && typeof step.newline !== 'boolean')
                        || (index < steps.length - 1 && (step.exitCode !== undefined || step.input === null))) {
                    throw new Error(`Invalid command ${index + 1}: ${scenario.id}`);
                }
            }
        }
    }
}

async function findStubs(directory) {
    const missing = [];
    for (const item of await readdir(directory, { withFileTypes: true })) {
        const file = path.join(directory, item.name);
        if (item.isDirectory()) missing.push(...await findStubs(file));
        else if (item.name.endsWith('.java')) {
            const text = await readFile(file, 'utf8');
            for (const match of text.matchAll(/throw new UnsupportedOperationException\("(TODO\([^"]+)"\)/g)) {
                missing.push(match[1]);
            }
        }
    }
    return missing;
}

async function build(record) {
    const isWindows = process.platform === 'win32';
    const command = isWindows ? 'cmd.exe' : path.join(ROOT, 'gradlew');
    const args = isWindows ? ['/d', '/c', 'gradlew.bat --console=plain shadowJar']
        : ['--console=plain', 'shadowJar'];
    record(`BUILD ${command} ${args.join(' ')}`);
    const child = new ConsoleProcess(command, args, ROOT, record);
    child.child.stdin.end();
    try {
        await child.waitUntil(() => child.closed, 180000);
        record(child.stdout);
        if (child.code !== 0) throw new Error('Current-code build failed');
    } finally {
        if (!child.closed && isWindows) {
            const killer = spawn('taskkill', ['/PID', String(child.child.pid), '/T', '/F'],
                { windowsHide: true, stdio: 'ignore' });
            await once(killer, 'close');
        }
        await child.stop();
    }
}

/** Runs one stateful case; restarts share only this case's isolated data. */
export async function runScenario(scenario, command, args, record) {
    const directory = await mkdtemp(path.join(tmpdir(), 'scheduleflow-ui-'));
    let position = 'setup';
    record(`CASE ${scenario.id}: ${scenario.aim}\nDIRECTORY ${directory}`);
    try {
        for (const [relative, content] of Object.entries(scenario.files ?? {})) {
            const file = path.resolve(directory, relative);
            if (!file.startsWith(directory + path.sep)) throw new Error('Setup path escapes case directory');
            await mkdir(path.dirname(file), { recursive: true });
            await writeFile(file, content, 'utf8');
        }
        for (const [sessionIndex, session] of scenario.sessions.entries()) {
            position = `session ${sessionIndex + 1} startup`;
            const child = new ConsoleProcess(command, args, directory, record);
            try {
                await child.frame(session.startup, session.exitCode ?? null);
                for (const [index, step] of session.steps.entries()) {
                    position = `session ${sessionIndex + 1} command ${index + 1} ${JSON.stringify(step.input)}`;
                    child.send(step.input);
                    await child.frame(step.output + (step.newline === false ? '' : '\n'), step.exitCode ?? null);
                }
            } finally {
                await child.stop();
            }
        }
        record(`PASSED ${scenario.id}`);
    } catch (error) {
        throw new Error(`FAILED ${scenario.id} ${position}\n${error.message}`);
    } finally {
        await rm(directory, { recursive: true, force: true });
    }
}

async function loadFixtures(args) {
    const options = {};
    for (let index = 0; index < args.length; index += 2) {
        if (!['--fixtures', '--commands', '--expected-outputs'].includes(args[index]) || !args[index + 1]) {
            throw new Error(`Unknown or incomplete argument: ${args[index]}`);
        }
        options[args[index]] = args[index + 1];
    }
    if (options['--commands'] || options['--expected-outputs']) {
        if (options['--fixtures']) throw new Error('Choose fixtures or command lists, not both');
        const commands = JSON.parse(options['--commands']);
        const outputs = JSON.parse(options['--expected-outputs']);
        if (!Array.isArray(commands) || !Array.isArray(outputs) || commands.length !== outputs.length) {
            throw new Error('Command and expected-output lists must have equal lengths');
        }
        return { version: 1, cases: [{ id: 'ADHOC', aim: 'Caller-supplied commands',
            status: 'active', setup: 'Empty isolated directory; final command must exit normally',
            sessions: [{ startup: '', steps: commands.map((input, index) => ({
                input, output: outputs[index], ...(index === commands.length - 1 ? { exitCode: 0 } : {})
            })) }] }] };
    }
    return JSON.parse(await readFile(path.resolve(ROOT,
        options['--fixtures'] ?? 'test/ui-scenarios.json'), 'utf8'));
}

async function main() {
    const transcript = [];
    const record = text => { transcript.push(text); console.log(text); };
    const transcriptDirectory = path.join(ROOT, 'build/ui-transcripts');
    const transcriptFile = path.join(transcriptDirectory,
        `ui-${new Date().toISOString().replaceAll(':', '-')}-${process.pid}.txt`);
    let exitCode = 1;
    try {
        const fixtures = await loadFixtures(process.argv.slice(2));
        validateFixtures(fixtures);
        const java = process.env.JAVA_HOME ? path.join(process.env.JAVA_HOME, 'bin/java') : 'java';
        const version = new ConsoleProcess(java, ['-version'], ROOT, record);
        try {
            await version.waitUntil(() => version.closed);
            if (version.code !== 0 || !/version "25(?:[."])/.test(version.stderr)) {
                throw new Error(`Java 25 required: ${version.stderr}`);
            }
        } finally {
            await version.stop();
        }
        await build(record);
        let missing = await findStubs(path.join(ROOT, 'src/main/java'));
        try {
            await readFile(path.join(ROOT, 'src/main/java/scheduleflow/Main.java'));
        } catch (error) {
            if (error.code !== 'ENOENT') throw error;
            missing = ['Printing: scheduleflow.Main and ScheduleFlow components not created', ...missing];
        }
        if (missing.length) {
            record(`BLOCKED: unfinished application components\n${missing.join('\n')}`);
            for (const scenario of fixtures.cases) record(`UNEXECUTED ${scenario.id} (${scenario.status})`);
            record('RESULT passed=0 failed=0; all listed cases blocked by scaffold preflight');
            exitCode = 2;
        } else {
            const active = fixtures.cases.filter(scenario => scenario.status === 'active');
            for (const scenario of fixtures.cases.filter(scenario => scenario.status === 'planned')) {
                record(`PLANNED ${scenario.id}: ${scenario.setup}`);
            }
            for (const scenario of active) {
                await runScenario(scenario, java, ['-jar', path.join(ROOT, 'build/libs/scheduleflow.jar')], record);
            }
            record(`RESULT passed=${active.length} failed=0 planned=${fixtures.cases.length - active.length}`);
            exitCode = active.length ? 0 : 2;
            if (!active.length) record('BLOCKED: no active scenarios; review plan readiness');
        }
    } catch (error) {
        record(error.stack);
        record('FAILED: session stopped immediately; no later scenarios executed');
    } finally {
        await mkdir(transcriptDirectory, { recursive: true });
        await writeFile(transcriptFile, transcript.join('\n') + '\n', 'utf8');
        console.log(`Transcript: ${transcriptFile}`);
    }
    process.exitCode = exitCode;
}

if (process.argv[1] && path.resolve(process.argv[1]) === fileURLToPath(import.meta.url)) await main();
