package com.example.example.rxjava.internal.operators

import com.example.example.rxjava.AbstractObservableWithUpstream
import com.example.example.rxjava.ObservableSource
import com.example.example.rxjava.Observer
import com.example.example.rxjava.Scheduler
import com.example.example.rxjava.disposables.Disposable
import com.example.example.rxjava.internal.BasicIntQueueDisposable
import com.example.example.rxjava.internal.QueueDisposable
import com.example.example.rxjava.internal.QueueFuseable

class ObservableObserveOn<T>(private val scheduler: Scheduler,val delayError:Boolean,val bufferSize:Int,override val source: ObservableSource<T>):AbstractObservableWithUpstream<T,T>(source){



    override fun subscribeActual(observer: Observer<in T>?) {


    }

    class ObserveOnObserver<T> :BasicIntQueueDisposable<T>(),Observer<T>,Runnable{


        override fun requestFusion(mode: Int): Int {
            if(mode and QueueFuseable.ASYNC!=0){

            }
        }

        override fun poll(): T {
        }

        override fun isEmpty(): Boolean {
        }

        override fun clear() {
        }

        override fun dispose() {
        }

        override fun isDisposed(): Boolean {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: T) {
        }

        override fun onError(e: Throwable) {
        }

        override fun onComplete() {
        }

        override fun run() {
        }

        override fun offer(e: T): Boolean {
            return super.offer(e)
        }

        override fun offer(v1: T, v2: T): Boolean {
            return super.offer(v1, v2)
        }
    }
}