package com.example.example.recyclerview

import androidx.recyclerview.widget.RecyclerView

class GapWorker : Runnable {

    var mPostTimeNs =0L
    var mFrameIntervalNs =0L
    var mRecyclerViews = ArrayList<MyRecyclerView>()
    private var mTasks = ArrayList<Task>()


    override fun run() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        val sGapWorker = ThreadLocal<GapWorker>()
    }

    class Task {
        var immediate = false
        var viewVelocity = 0
        var distanceToItem = 0
        var view: RecyclerView? = null
        var position = 0

        fun clear() {
            immediate = false
            viewVelocity = 0
            distanceToItem = 0
            view = null
            position = 0
        }
    }

    class LayoutPrefetchRegistryImpl{

    }


}