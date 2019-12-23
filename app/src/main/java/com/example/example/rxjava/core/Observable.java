package com.example.example.rxjava.core;


import androidx.annotation.NonNull;

import com.example.example.rxjava.ObservableSource;
import com.example.example.rxjava.Observer;

import org.jetbrains.annotations.NotNull;

public abstract class Observable<T> implements ObservableSource<T> {


    protected abstract void subscribeActual(Observer<? super T> observer);


    static final class SubscribeOnObserver<T>{

    }

    @Override
    public void subscribe(@NonNull @NotNull Observer<? super T> observer) {
        subscribeActual(observer);
    }
}
