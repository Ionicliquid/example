package com.example.example.recyclerview

class  GapWorker :Runnable{


    override fun run() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object{
        val  sGapWorker = ThreadLocal<GapWorker>()
    }


}