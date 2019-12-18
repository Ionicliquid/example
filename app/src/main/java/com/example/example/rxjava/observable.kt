package com.example.example.rxjava




abstract class AbstractObservableWithUpstream<T, U>(protected val source: ObservableSource<T>) :Observable<U>() , HasUpstreamObservableSource<T>{


    override fun source(): ObservableSource<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return source
    }

}