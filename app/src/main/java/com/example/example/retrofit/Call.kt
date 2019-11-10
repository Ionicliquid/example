package com.example.example.retrofit


import okhttp3.Request
import okhttp3.ResponseBody
import java.io.IOException


interface Call<T> : Cloneable {

    fun execute(): Response<T>

    fun enqueue(callback: Callback<T>)

    fun isExecuted(): Boolean

    fun cancel()

    fun isCanceled(): Boolean

    override fun clone(): Call<T>

    fun request(): Request

}

interface Callback<T> {
    fun onResponse(call: Call<T>)
    fun onFailure(call: Call<T>, t: Throwable)
}

interface Converter<F,T>{
    fun convert(value:F):T

    abstract class Fatory{
        fun responseBodyConverter():Converter<ResponseBody,*>?{
            return null
        }
        
    }
}
