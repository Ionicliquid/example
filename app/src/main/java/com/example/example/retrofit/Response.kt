package com.example.example.retrofit

import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Request

import okhttp3.ResponseBody
import java.lang.IllegalArgumentException

class Response<T>(val rawResponse: okhttp3.Response, val body: T?, val errorBody: ResponseBody?) {


}