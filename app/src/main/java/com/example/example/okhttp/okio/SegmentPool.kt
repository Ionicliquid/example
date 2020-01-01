package com.example.example.okhttp.okio

class SegmentPool private constructor() {


    companion object {
        val MAX_SIZE = (64 * 1024).toLong()

        var next: Segment? = null

        var byteCount: Long = 0

        fun take(): Segment {
            synchronized(SegmentPool::class.java) {
                if (next != null) {
                    val result = next
                    next = result!!.next
                    result!!.next = null
                    byteCount -= Segment.SIZE.toLong()
                    return result
                }
            }
            return Segment() // Pool is empty. Don't zero-fill while holding a lock.
        }

        fun recycle(segment: Segment) {
            require(!(segment.next != null || segment.prev != null))
            if (segment.shared) return  // This segment cannot be recycled.
            synchronized(SegmentPool::class.java) {
                if (byteCount + Segment.SIZE > MAX_SIZE) return  // Pool is full.
                byteCount += Segment.SIZE.toLong()
                segment.next = next
                segment.limit = 0
                segment.pos = segment.limit
                next = segment
            }
        }
    }

}