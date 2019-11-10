//package com.example.example.retrofit;
//
//import androidx.annotation.Nullable;
//
//import okhttp3.Headers;
//import okhttp3.Protocol;
//import okhttp3.Request;
//
//public final class Response<T> {
//
//   public static <T> Response<T> success(@Nullable T body){
//
//       return success(body,new okhttp3.Response.Builder()
//       .code(200).message("OK").protocol(Protocol.HTTP_1_1)
//               .request(new Request.Builder().url("http://localhost/").build())
//                       .build()
//       );
//    }
//
//
//    public static <T> Response<T> success(int code ,@Nullable T body){
//       if(code<200||code>=300){
//           throw  new IllegalArgumentException("code<200 or code>=300: "+code);
//       }
//        return success(body, new okhttp3.Response.Builder() //
//                .code(code)
//                .message("Response.success()")
//                .protocol(Protocol.HTTP_1_1)
//                .request(new Request.Builder().url("http://localhost/").build())
//                .build());
//    }
//
//    public static <T> Response<T> success(@Nullable T body, Headers headers){
//       Utils.checkNotnull(headers,"headers ==null");
//        return success(body, new okhttp3.Response.Builder() //
//                .code(200)
//                .message("Response.success()")
//                .protocol(Protocol.HTTP_1_1)
//                .request(new Request.Builder().url("http://localhost/").build())
//                .build());
//    }
//
//
//    public static <T> Response<T>  success(@Nullable T body, okhttp3.Response rawResponse){
//       Utils.checkNotnull(rawResponse,"rawResponse == null");
//       if(!rawResponse.isSuccessful()){
//           throw new IllegalArgumentException("rawresponse must be successful response");
//
//       }
//       return new Response<>()(rawResponse,body,null);
//    }
//
//    private final okhttp3.Response rawR
//}
