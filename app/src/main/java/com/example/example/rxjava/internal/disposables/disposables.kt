package com.example.example.rxjava.internal.disposables

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.example.example.rxjava.disposables.Disposable
import io.reactivex.rxjava3.exceptions.ProtocolViolationException
import io.reactivex.rxjava3.internal.functions.ObjectHelper
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.util.concurrent.atomic.AtomicReference

enum class DisposableHelper : Disposable {

    DISPOSED;


    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isDisposed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return true
    }


    companion object {
        fun isDisposed(d: Disposable): Boolean {
            return d == DISPOSED
        }

        fun trySet(
            field: AtomicReference<Disposable>,
            d: Disposable
        ): Boolean {
            if (!field.compareAndSet(null, d)) {
                if (field.get() === DISPOSED) {
                    d.dispose()
                }
                return false
            }
            return true
        }

        fun reportDisposableSet() {
            RxJavaPlugins.onError(ProtocolViolationException("Disposable already set!"))
        }

        fun validate(
            current: Disposable?,
            next: Disposable?
        ): Boolean {
            if (next == null) {
                RxJavaPlugins.onError(NullPointerException("next is null"))
                return false
            }
            if (current != null) {
                next.dispose()
                reportDisposableSet()
                return false
            }
            return true
        }

        fun dispose(field: AtomicReference<Disposable>): Boolean {
            var current: Disposable? = field.get()
            val d = DISPOSED
            if (current !== d) {
                current = field.getAndSet(d)
                if (current !== d) {
                    current?.dispose()
                    return true
                }
            }
            return false
        }

        fun replace(
            field: AtomicReference<Disposable>,
            d: Disposable?
        ): Boolean {
            while (true) {
                val current = field.get()
                if (current === DISPOSED) {
                    d?.dispose()
                    return false
                }
                if (field.compareAndSet(current, d)) {
                    return true
                }
            }
        }

        fun setOnce(
            field: AtomicReference<Disposable>,
            d: Disposable
        ): Boolean {
            ObjectHelper.requireNonNull(d, "d is null")
            if (!field.compareAndSet(null, d)) {
                d.dispose()
                if (field.get() !== DISPOSED) {
                    reportDisposableSet()
                }
                return false
            }
            return true
        }

        operator fun set(
            field: AtomicReference<Disposable>,
            d: Disposable?
        ): Boolean {
            while (true) {
                val current = field.get()
                if (current === DISPOSED) {
                    d?.dispose()
                    return false
                }
                if (field.compareAndSet(current, d)) {
                    current?.dispose()
                    return true
                }
            }
        }

    }
}



class SequentialDisposable() :AtomicReference<Disposable>(),Disposable, Parcelable {

    private val serialVersionUID = -754898800686245608L

    constructor(parcel: Parcel) : this() {
    }


    constructor(disposable: SequentialDisposable) :this(){

    }

    override fun dispose() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isDisposed(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SequentialDisposable> {
        override fun createFromParcel(parcel: Parcel): SequentialDisposable {
            return SequentialDisposable(parcel)
        }

        override fun newArray(size: Int): Array<SequentialDisposable?> {
            return arrayOfNulls(size)
        }
    }

}