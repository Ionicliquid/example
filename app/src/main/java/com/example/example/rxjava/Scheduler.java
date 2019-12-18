package com.example.example.rxjava;

import java.util.concurrent.TimeUnit;

public abstract class Scheduler {

    static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
    static {
        CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(
                Long.getLong("rx3.scheduler.drift-tolerance", 15));
    }

}
