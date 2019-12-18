package com.example.example.rxjava.functions

import io.reactivex.rxjava3.annotations.NonNull

interface Function3<T1,T2,T3,R>{

    @NonNull
    @Throws(Throwable::class)
    fun apply(@NonNull t1: T1, @NonNull t2: T2, @NonNull t3: T3): R
}

