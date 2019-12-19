package com.example.example.rxjava.disposables

import io.reactivex.rxjava3.exceptions.Exceptions
import io.reactivex.rxjava3.internal.util.ExceptionHelper
import io.reactivex.rxjava3.internal.util.OpenHashSet
import java.util.ArrayList


interface DisposableContainer {

    fun add(d: Disposable): Boolean

    fun remove(d: Disposable): Boolean

    fun delete(d: Disposable): Boolean

}

class CompositeDisposable : Disposable, DisposableContainer {

    var resources: OpenHashSet<Disposable>? = null

    @Volatile
    var disposed: Boolean = false

    constructor(vararg disposables: Disposable) {
        resources = OpenHashSet(disposables.size + 1)
        for (disposable in disposables) {
            resources!!.add(disposable)
        }
    }


    override fun dispose() {
        if (disposed) {
            return
        }
        var set: OpenHashSet<Disposable>
        synchronized(this) {
            if (disposed) {
                return
            }
            disposed = true
            resources?.let {
                set = it
            }

        }
    }

    override fun isDisposed(): Boolean {
        return disposed
    }

    override fun add(d: Disposable): Boolean {
        if (!disposed) {
            synchronized(this) {
                if (!disposed) {
                    var set = resources
                    if (set == null) {
                        set = OpenHashSet()
                        resources = set
                    }
                    set.add(d)
                    return true
                }
            }
        }
        d.dispose()
        return false
    }

    override fun remove(d: Disposable): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(d: Disposable): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun dispose(set:OpenHashSet<Disposable>){
        if(set== null){
            return
        }
        var errors: MutableList<Throwable>? = null
        val array = set.keys()
        for (o in array) {
            if (o is io.reactivex.rxjava3.disposables.Disposable) {
                try {
                    o.dispose()
                } catch (ex: Throwable) {
                    Exceptions.throwIfFatal(ex)
                    if (errors == null) {
                        errors = ArrayList()
                    }
                    errors.add(ex)
                }

            }
        }
        if (errors != null) {
            if (errors.size == 1) {
                throw ExceptionHelper.wrapOrThrow(errors[0])
            }
//            throw CompositeException(errors)
        }
    }
}