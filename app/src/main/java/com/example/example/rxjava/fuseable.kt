package com.example.example.rxjava

interface HasUpstreamObservableSource<T>{

    fun source():ObservableSource<T>
}