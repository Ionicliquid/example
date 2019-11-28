package com.example.example.retrofit

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request


interface Call<T> :Cloneable{

    fun execute():Response<T>

    fun enqueue(callback: Callback)

    fun isExecuted():Boolean

    fun cancel()

    fun isCanceled():Boolean

    override fun clone():Call

    fun request():Request



}