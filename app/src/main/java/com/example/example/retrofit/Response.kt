package com.example.example.retrofit

import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Request

import okhttp3.ResponseBody
import java.lang.IllegalArgumentException

class Response<T>(val rawResponse: okhttp3.Response, val body: T?, val errorBody: ResponseBody?) {

    fun code() = rawResponse.code()
    fun message() = rawResponse.message()
    fun headers(): Headers = rawResponse.headers()
    fun isSuccessful() = rawResponse.isSuccessful()
    fun body() = rawResponse.body()

    companion object {
        fun <T> success(body: T, rawResponse: okhttp3.Response): Response<T> {
            Utils.checkNotnull(rawResponse, "rawResponse ==null")
            if (!rawResponse.isSuccessful()) {
                throw IllegalArgumentException("rawResponse must be successful response")
            }
            return Response(rawResponse, body, null)
        }

        fun <T> success(body: T): Response<T> {
            return success(
                body, okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        fun <T> success(body: T,code:Int): Response<T> {
            if (code < 200 || code >= 300) {
                throw IllegalArgumentException("code < 200 or >= 300: " + code);
            }
            return success(
                body, okhttp3.Response.Builder()
                    .code(code)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        fun <T> success(body: T,headers: Headers): Response<T> {
            Utils.checkNotnull(headers, "headers == null")
            return success(
                body, okhttp3.Response.Builder()
                    .code(200)
                    .message("OK")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }

        fun <T> error(body: ResponseBody,rawResponse: okhttp3.Response):Response<T>{
            Utils.checkNotnull(body,"body==null" )
            Utils.checkNotnull(rawResponse,"rawResponse == null")
            require(!rawResponse.isSuccessful) { "rawResponse should not be successful response" }
            return Response<T>(rawResponse, null, body)
        }

        fun <T> error(code: Int, body: ResponseBody): Response<T> {
            Utils.checkNotnull(body, "body == null")
            require(code >= 400) { "code < 400: $code" }
            return error(
                body, okhttp3.Response.Builder() //
                    .body(
                        OkHttpCall.NoContentResponseBody(
                            body.contentType(),
                            body.contentLength()
                        )
                    )
                    .code(code)
                    .message("Response.error()")
                    .protocol(Protocol.HTTP_1_1)
                    .request(Request.Builder().url("http://localhost/").build())
                    .build()
            )
        }
    }
}