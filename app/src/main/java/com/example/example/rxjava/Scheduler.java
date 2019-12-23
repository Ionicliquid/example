package com.example.example.rxjava;



import com.example.example.rxjava.disposables.Disposable;
import com.example.example.rxjava.internal.disposables.SequentialDisposable;
import com.example.example.rxjava.schedulers.SchedulerRunnableIntrospection;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

import io.reactivex.rxjava3.internal.disposables.EmptyDisposable;

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


        @NonNull
        public Disposable schedulePeriodically(@NonNull Runnable run, final long initialDelay, final long period, @NonNull final TimeUnit unit) {
            final SequentialDisposable first = new SequentialDisposable();

            final SequentialDisposable sd = new SequentialDisposable(first);

            final Runnable decoratedRun = RxJavaPlugins.onSchedule(run);

            final long periodInNanoseconds = unit.toNanos(period);
            final long firstNowNanoseconds = now(TimeUnit.NANOSECONDS);
            final long firstStartInNanoseconds = firstNowNanoseconds + unit.toNanos(initialDelay);

            Disposable d = schedule(new PeriodicTask(firstStartInNanoseconds, decoratedRun, firstNowNanoseconds, sd,
                    periodInNanoseconds), initialDelay, unit);

            if (d == EmptyDisposable.INSTANCE) {
                return d;
            }
            first.replace(d);

            return sd;
        }

    }

    public static long now(@NonNull TimeUnit unit) {
        return unit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
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


    final class PeriodicTask implements Runnable,SchedulerRunnableIntrospection{
        @NotNull
        @Override
        public Runnable getWrappedRunnable() {
            return null;
        }

        @Override
        public void run() {

        }
    }

    final class PeriodicDirectTask implements Runnable,SchedulerRunnableIntrospection,Disposable{
        @Override
        public void dispose() {

        }

        @Override
        public boolean isDisposed() {
            return false;
        }

        @NotNull
        @Override
        public Runnable getWrappedRunnable() {
            return null;
        }

        @Override
        public void run() {

        }
    }

}
