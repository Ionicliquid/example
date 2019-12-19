package com.example.example.rxjava;



import com.example.example.rxjava.disposables.Disposable;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public abstract class Scheduler {

    static final long CLOCK_DRIFT_TOLERANCE_NANOSECONDS;

    static {
        CLOCK_DRIFT_TOLERANCE_NANOSECONDS = TimeUnit.MINUTES.toNanos(
                Long.getLong("rx3.scheduler.drift-tolerance", 15));
    }

    public static long clockDriftTolerance() {
        return CLOCK_DRIFT_TOLERANCE_NANOSECONDS;
    }

    public void start() {

    }

    @NonNull
    public abstract Worker createWorker();

    public void shutdown() {

    }

    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable run) {
        return scheduleDirect(run, 0L, TimeUnit.NANOSECONDS);
    }


    @NonNull
    public Disposable scheduleDirect(@NonNull Runnable run, long delay, @NonNull TimeUnit unit) {
        final Worker w = createWorker();

        final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);

        DisposeTask task = new DisposeTask(decoratedRun, w);

        w.schedule(task, delay, unit);

        return task;
    }

    public abstract static class Worker implements Disposable {


        @NonNull
        public Disposable schedule(@NonNull Runnable run) {
            return schedule(run, 0L, TimeUnit.NANOSECONDS);
        }

        @NonNull
        public abstract Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit);


    }

    static final class DisposeTask implements Disposable, Runnable, SchedulerRunnableIntrospection {

        @Nullable
        Thread runner;
        @NonNull
        final Runnable decoration;
        @NonNull
        final Worker worker;

        public DisposeTask(Runnable decoration, Worker worker) {
            this.decoration = decoration;
            this.worker = worker;
        }

        @Override
        public void dispose() {

        }

        @Override
        public boolean isDisposed() {
            return worker.isDisposed();
        }

        @NotNull
        @Override
        public Runnable getWrappedRunnable() {

            return this.decoration;
        }

        @Override
        public void run() {
            runner = Thread.currentThread();
            try {
                decoration.run();
            }finally {
                dispose();
                runner = null;
            }
        }
    }

}
