package com.example.example.rxjava.internal

import com.example.example.rxjava.Scheduler
import com.example.example.rxjava.schedulers.NewThreadWorker


class IoScheduler :Scheduler(){


    override fun createWorker(): Worker {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    class CacheWorkerPool :Runnable{


        override fun run() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    class TreadWorker : NewThreadWorker() {

    }

}