package com.example.example.retrofit

import okhttp3.Response
import okhttp3.ResponseBody

class Response<T>(val response: Response,var body: ResponseBody,var t:T){

    companion object{
        fun success(){

        }
    }
}