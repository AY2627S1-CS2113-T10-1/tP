package scheduleflow.cli;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Defines the complete command vocabulary as immutable transport values.
 */
public sealed interface Command {
    /**
     * Requests command help.
     */
    record Help() implements Command {
        public Help {
        }
    }

    /**
     * Carries parsed task fields without domain or future-deadline validation.
     */
    record AddTask(String name, LocalDateTime deadline, int minutes) implements Command {
        public AddTask {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(deadline, "deadline");
        }
    }

    /**
     * Requests all unfinished tasks.
     */
    record ListTasks() implements Command {
        public ListTasks {
        }
    }

    /**
     * Carries the numeric suffix of a task ID.
     */
    record DeleteTask(int id) implements Command {
        public DeleteTask {
        }
    }

    /**
     * Carries parsed weekly commitment fields without overlap validation.
     */
    record AddCommitment(String name, DayOfWeek day, LocalTime start, int minutes) implements Command {
        public AddCommitment {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(day, "day");
            Objects.requireNonNull(start, "start");
        }
    }

    /**
     * Requests all recurring weekly commitments.
     */
    record ListCommitments() implements Command {
        public ListCommitments {
        }
    }

    /**
     * Carries the numeric suffix of a commitment ID.
     */
    record DeleteCommitment(int id) implements Command {
        public DeleteCommitment {
        }
    }

    /**
     * Requests replacement of the temporary plan.
     */
    record GeneratePlan() implements Command {
        public GeneratePlan {
        }
    }

    /**
     * Defers resolving today until dispatch captures the clock.
     */
    record ScheduleToday() implements Command {
        public ScheduleToday {
        }
    }

    /**
     * Requests a schedule projection for an explicit date.
     */
    record ScheduleDate(LocalDate date) implements Command {
        public ScheduleDate {
            Objects.requireNonNull(date, "date");
        }
    }

    /**
     * Requests normal exit without another save.
     */
    record Exit() implements Command {
        public Exit {
        }
    }
}
