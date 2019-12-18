package com.example.example.rxjava;

import io.reactivex.rxjava3.annotations.NonNull;

import java.util.concurrent.TimeUnit;

public abstract class Worker implements Disposable {


    @NonNull
    public Disposable schedule(@NonNull Runnable run) {
        return schedule(run, 0L, TimeUnit.NANOSECONDS);
    }

    @NonNull
    public abstract Disposable schedule(@NonNull Runnable run, long delay, @NonNull TimeUnit unit);

}
