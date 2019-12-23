package com.example.example.rxjava.schedulers

import com.example.example.rxjava.disposables.Disposable
import com.example.example.rxjava.Scheduler
import com.example.example.rxjava.internal.SchedulerPoolFactory
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit

class NewThreadWorker(threadFactory: ThreadFactory) :Scheduler.Worker(),
    Disposable {

    private val executor:ScheduledExecutorService =
        SchedulerPoolFactory.create(threadFactory)

    @Volatile
    var disposed: Boolean = false

    override fun dispose() {
        if(!disposed){
            disposed = true
            executor.shutdown()
        }
    }

    override fun isDisposed(): Boolean {
        return disposed
    }

    override fun schedule(run: Runnable?): Disposable {
        return schedule(run ,0,null)
    }

    override fun schedule(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {

    }
}