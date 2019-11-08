package com.example.example.retrofit

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response

interface Call<out T> :Cloneable{

    fun execute():Response

    fun enqueue(callback: Callback)

    fun isExecuted():Boolean

    fun cancel()

    fun isCanceled():Boolean

    override fun clone():Call

    fun request():Request



}