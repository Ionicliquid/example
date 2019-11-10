package com.example.example.retrofit

import okhttp3.Request
import java.util.*

class OKHttpCall<T>() :Call<T>{




    override fun execute(): Response<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun enqueue(callback: Callback<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isExecuted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isCanceled(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone(): Call<T> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun request(): Request {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}