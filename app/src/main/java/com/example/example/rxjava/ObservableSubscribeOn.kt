package com.example.example.rxjava

class ObservableSubscribeOn<T>(source: ObservableSource<T>,val scheduler: Scheduler) :AbstractObservableWithUpstream<T,T>(source){



    override fun subscribeActual(observer: Observer<in T>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun subscribe(observer: Observer<in T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

    internal class SubscribeOnObserver<T>{

    }
}


