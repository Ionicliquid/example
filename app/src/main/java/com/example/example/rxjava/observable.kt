package com.example.example.rxjava

import com.example.example.rxjava.core.Observable
import com.example.example.rxjava.internal.HasUpstreamObservableSource


abstract class AbstractObservableWithUpstream<T, U>(protected open val source: ObservableSource<T>) :
    Observable<U>() , HasUpstreamObservableSource<T> {


    override fun source(): ObservableSource<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return source
    }

}