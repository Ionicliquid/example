package com.example.example.rxjava;

import androidx.annotation.NonNull;

public interface Observer<T> {

    void onSubscribe(@NonNull Disposable d);

    void onNext(@NonNull T t);

    void onError(@NonNull Throwable e);

    void onComplete();
}
