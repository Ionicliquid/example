package com.example.example.rxjava


import com.example.example.rxjava.disposables.Disposable
import com.example.example.rxjava.internal.disposables.DisposableHelper
import java.util.concurrent.atomic.AtomicReference

class ObservableSubscribeOn<T>(source: ObservableSource<T>, private val scheduler: Scheduler) :
    AbstractObservableWithUpstream<T, T>(source) {


    override fun subscribeActual(observer: Observer<in T>) {
        val parent = SubscribeOnObserver(observer)
        observer.onSubscribe(parent)
        parent.set(scheduler.scheduleDirect(SubscribeTask(parent)))
    }


    class SubscribeOnObserver<T>(private val downstream: Observer<in T>) :
        AtomicReference<Disposable>(), Observer<T>,
        Disposable {

        private val serialVersionUID = 8094547886072529208L


        private val upstream: AtomicReference<Disposable> = AtomicReference()


        override fun onSubscribe(d: Disposable) {
            DisposableHelper.setOnce(this.upstream, d)
        }

        override fun onNext(t: T) {
            downstream.onNext(t)
        }

        override fun onError(e: Throwable) {
            downstream.onError(e)
        }

        override fun onComplete() {
            downstream.onComplete()
        }

        override fun dispose() {
            DisposableHelper.dispose(upstream)
            DisposableHelper.dispose(this)
        }

        override fun isDisposed(): Boolean {
            return DisposableHelper.isDisposed(get())
        }

    }

    inner class SubscribeTask constructor(private val parent: SubscribeOnObserver<T>) : Runnable {

        override fun run() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            source.subscribe(parent)
        }
    }


}


