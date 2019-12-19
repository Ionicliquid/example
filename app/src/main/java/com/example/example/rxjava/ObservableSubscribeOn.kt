package com.example.example.rxjava


import com.example.example.rxjava.disposables.Disposable
import java.util.concurrent.atomic.AtomicReference

class ObservableSubscribeOn<T>(source: ObservableSource<T>,val scheduler: Scheduler) :AbstractObservableWithUpstream<T,T>(source){



    override fun subscribeActual(observer: Observer<in T>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun subscribe(observer: Observer<in T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    class SubscribeOnObserver<T>(private val downstream: Observer<in T>):AtomicReference<Disposable>(),Observer<T>,
        Disposable {

        private val serialVersionUID = 8094547886072529208L


        private val upstream: AtomicReference<Disposable> = AtomicReference()


        override fun onSubscribe(d: Disposable) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onNext(t: T) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            downstream.onNext(t)
        }

        override fun onError(e: Throwable) {
            downstream.onError(e)
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onComplete() {
            downstream.onComplete()
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun dispose() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            DisposableHelper.dispose(upstream)
            DisposableHelper.dispose(this)
        }

        override fun isDisposed(): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    inner class SubscribeTask constructor(val parent:SubscribeOnObserver<T>):Runnable{

        override fun run() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            source.subscribe(parent)
        }
    }
}


