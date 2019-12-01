package com.example.example.okhttp

import okhttp3.Request
import okhttp3.Response
import okio.Timeout

interface Call :Cloneable{
    fun request(): Request

    fun execute():Response

    fun enqueue(responseCallback: Callback)

    fun cancel()

    fun isExecuted():Boolean

    fun isCanceled():Boolean

    fun timeout():Timeout

    override fun clone(): Call

    interface Factory{
        fun newCall(request: Request):Call
    }


}