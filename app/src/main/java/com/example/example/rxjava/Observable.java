package com.example.example.rxjava;


public abstract class Observable<T> implements ObservableSource<T> {


    protected abstract void subscribeActual(Observer<? super T> observer);


    static final class SubscribeOnObserver<T>{

    }


}
