package me.belakede.time;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * http://stackoverflow.com/a/29360514
 */
public class TimeMachine {

    private static Clock clock = Clock.systemDefaultZone();
    private static ZoneId zoneId = ZoneId.systemDefault();

    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public static void useFixedClockAt(LocalDateTime date) {
        clock = Clock.fixed(date.atZone(zoneId).toInstant(), zoneId);
    }

    public static void useSystemDefaultZoneClock() {
        clock = Clock.systemDefaultZone();
    }

    private static Clock getClock() {
        return clock;
    }
}