package scheduleflow.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;
import scheduleflow.common.InputRules;
import scheduleflow.common.ValidationException;
import scheduleflow.planning.TimeRules;

/**
 * Stores a weekly half-open busy interval which may end at, but never cross, midnight.
 */
public record Commitment(int id, String name, DayOfWeek day, LocalTime start, int durationMinutes) {
    private static final int MINUTES_PER_HOUR = 60;
    private static final int MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR;

    /**
     * Creates an aligned commitment and normalizes its name.
     */
    public Commitment {
        if (id <= 0 || day == null || start == null) {
            throw new ValidationException("Commitment requires a positive ID, weekday and start time.");
        }
        name = InputRules.normalizeName(name);
        InputRules.requireDuration(durationMinutes);
        if (start.getSecond() != 0 || start.getNano() != 0 || start.getMinute() % TimeRules.SLOT_MINUTES != 0) {
            throw new ValidationException("Commitment start must be on a half-hour boundary.");
        }
        int startMinute = start.getHour() * MINUTES_PER_HOUR + start.getMinute();
        // Subtraction avoids overflowing when the supplied duration is near Integer.MAX_VALUE.
        if (durationMinutes > MINUTES_PER_DAY - startMinute) {
            throw new ValidationException("Commitment must not continue past midnight.");
        }
    }

    /**
     * Returns the stable commitment identifier with no zero padding.
     */
    public String displayId() {
        return "C" + id;
    }

    /**
     * Returns the exclusive endpoint, using 1440 for midnight rather than wrapping to zero.
     */
    public int endMinuteOfDay() {
        return start.getHour() * MINUTES_PER_HOUR + start.getMinute() + durationMinutes;
    }

    /**
     * Tests intersection on the same weekday; adjacent endpoints do not overlap.
     */
    public boolean overlaps(Commitment other) {
        Objects.requireNonNull(other, "other");
        int thisStart = endMinuteOfDay() - durationMinutes;
        int otherStart = other.endMinuteOfDay() - other.durationMinutes;
        return day == other.day && thisStart < other.endMinuteOfDay() && otherStart < endMinuteOfDay();
    }
}
