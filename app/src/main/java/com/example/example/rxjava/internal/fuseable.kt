package com.example.example.rxjava.internal

import com.example.example.rxjava.ObservableSource
import com.example.example.rxjava.disposables.Disposable
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.annotations.Nullable
import java.util.concurrent.atomic.AtomicInteger

interface HasUpstreamObservableSource<T> {

    fun source(): ObservableSource<T>
}

abstract class BasicIntQueueDisposable<T>:AtomicInteger(),QueueDisposable<T>{

    private val serialVersionUID = -1001730202384742097L

    override fun offer(e: T): Boolean {
        throw UnsupportedOperationException("Should not be called")
    }


    override fun offer(v1: T, v2: T): Boolean {
        throw UnsupportedOperationException("Should not be called")
    }

    override fun toByte(): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toChar(): Char {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toShort(): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toDouble(): Double {
        return super.toDouble()
    }

    override fun toFloat(): Float {
        return super.toFloat()
    }

    override fun toLong(): Long {
        return super.toLong()
    }

    override fun toString(): String {
        return super<AtomicInteger>.toString()
    }

    override fun toInt(): Int {
        return super.toInt()
    }
}

interface QueueDisposable<T> :QueueFuseable<T>,Disposable


interface QueueFuseable<T> :SimpleQueue<T>{


     fun requestFusion(mode: Int): Int


    companion object{
        val NONE= 0

        val SYNC = 1

        val ASYNC = 2

        val ANY = SYNC or ASYNC //3

        val BOUNDARY = 4

    }

}


interface SimpleQueue<T> {

    fun offer(@NonNull value: T): Boolean


    fun offer(@NonNull v1: T, @NonNull v2: T): Boolean

    @Nullable
    @Throws(Throwable::class)
    fun poll(): T

    fun isEmpty(): Boolean


    fun clear()
}