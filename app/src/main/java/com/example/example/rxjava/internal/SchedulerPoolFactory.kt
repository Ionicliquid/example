package com.example.example.rxjava.internal

import android.os.Build
import androidx.annotation.RequiresApi
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.internal.schedulers.RxThreadFactory
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicReference

object SchedulerPoolFactory {


    val PURGE_THREAD = AtomicReference<ScheduledExecutorService>()

    val POOLS = ConcurrentHashMap<ScheduledThreadPoolExecutor, Any>()

    val PURGE_ENABLED_KEY = "rx3.purge-enabled"

    val PURGE_ENABLED: Boolean

    val PURGE_PERIOD_SECONDS_KEY = "rx3.purge-period-seconds"

    val PURGE_PERIOD_SECONDS:Int

    init {
        val propertyAccessor = SystemPropertyAccessor()
        PURGE_ENABLED = getBooleanProperty(true, PURGE_ENABLED_KEY, true, true, propertyAccessor)
        PURGE_PERIOD_SECONDS = getIntProperty(PURGE_ENABLED, PURGE_PERIOD_SECONDS_KEY, 1, 1, propertyAccessor)

        start()
    }

    fun tryStart(purgeEnabled: Boolean) {
        if (purgeEnabled) {
            while (true) {
                val get = PURGE_THREAD.get()
                if (get != null) {
                    return
                }
                val next: ScheduledExecutorService =
                    Executors.newScheduledThreadPool(1, RxThreadFactory("RxSchedulerPurge"))
                if (PURGE_THREAD.compareAndSet(get, next)) {
                    next.scheduleAtFixedRate(
                        ScheduledTask(),
                        PURGE_PERIOD_SECONDS.toLong(), PURGE_PERIOD_SECONDS.toLong(), TimeUnit.SECONDS
                    )
                    return

                } else {
                    next.shutdown()
                }


            }
        }
    }

    fun shutdown() {
        val exec = PURGE_THREAD.getAndSet(null)
        exec?.shutdownNow()
        POOLS.clear()
    }

    fun getBooleanProperty(
        enabled: Boolean,
        key: String,
        defaultNotFound: Boolean,
        defaultNotEnabled: Boolean,
        propertyAccessor: Function<String, String>
    ): Boolean {
        if (enabled) {
            try {
                val value = propertyAccessor.apply(key) ?: return defaultNotFound
                return "true" == value
            } catch (ex: Throwable) {
                return defaultNotFound
            }

        }
        return defaultNotEnabled
    }

    fun create(factory: ThreadFactory): ScheduledExecutorService {
        val exec = Executors.newScheduledThreadPool(1, factory)
        tryPutIntoPool(PURGE_ENABLED, exec)
        return exec
    }

    private fun tryPutIntoPool(purgeEnabled: Boolean, exec: ScheduledExecutorService) {
        if (purgeEnabled && exec is ScheduledThreadPoolExecutor) {
           POOLS[exec] = exec
        }
    }

    fun getIntProperty(
        enabled: Boolean,
        key: String,
        defaultNotFound: Int,
        defaultNotEnabled: Int,
        propertyAccessor: Function<String, String>
    ): Int {
        if (enabled) {
            try {
                val value = propertyAccessor.apply(key) ?: return defaultNotFound
                return Integer.parseInt(value)
            } catch (ex: Throwable) {
                return defaultNotFound
            }

        }
        return defaultNotEnabled
    }

    class SystemPropertyAccessor : Function<String, String> {
        @Throws(Throwable::class)
        override fun apply(t: String): String? {
            return System.getProperty(t)
        }
    }

    fun start() {
        tryStart(PURGE_ENABLED)
    }

}

class ScheduledTask : Runnable {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun run() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        val list = ArrayList<ScheduledThreadPoolExecutor>(SchedulerPoolFactory.POOLS.keys)
        for (e in list) {
            if (e.isShutdown) {
                SchedulerPoolFactory.POOLS.remove(e)
            } else {
                e.purge()
            }
        }

    }

}