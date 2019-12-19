package com.example.example.rxjava

import io.reactivex.rxjava3.annotations.NonNull

interface SchedulerRunnableIntrospection {
    @NonNull
    fun getWrappedRunnable(): Runnable
}