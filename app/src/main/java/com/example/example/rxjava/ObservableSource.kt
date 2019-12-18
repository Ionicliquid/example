package com.example.example.rxjava

import androidx.annotation.NonNull


interface ObservableSource<T>{

    fun subscribe(@NonNull observer: Observer<in T>)
}