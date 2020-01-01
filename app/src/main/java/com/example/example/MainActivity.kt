package com.example.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.internal.operators.observable.ObservableObserveOn
import io.reactivex.rxjava3.internal.operators.observable.ObservableSubscribeOn
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var recyclerView = RecyclerView(this)
        val o1 = Observable.create(ObservableOnSubscribe<String> {

        })
        val o2 :ObservableSubscribeOn<String> = o1.subscribeOn(Schedulers.newThread()) as ObservableSubscribeOn<String>


        val o3:ObservableObserveOn<String> = o2.observeOn(Schedulers.io()) as ObservableObserveOn<String>

        o3.subscribe(object : Observer<String> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable?) {
            }

            override fun onNext(t: String?) {
            }

            override fun onError(e: Throwable?) {
            }
        })


    }
}
