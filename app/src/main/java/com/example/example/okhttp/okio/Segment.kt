package com.example.example.okhttp.okio

class Segment {

    companion object{
        const val SIZE = 8192

        const val SHARE_MINIMUM = 1024
    }

    val data: ByteArray

    var pos: Int = 0

    var limit: Int = 0

    var shared: Boolean = false

    var owner: Boolean = false

     var next: Segment? = null

    var prev: Segment? = null

    constructor(){
        data = ByteArray(SIZE)
        owner = true
        shared = false
    }

    constructor(data: ByteArray, pos: Int, limit: Int, shared: Boolean, owner: Boolean){
        this.data = data
        this.pos = pos
        this.limit = limit
        this.shared = shared
        this.owner = owner
    }

    fun sharedCopy(): Segment {
        shared = true
        return Segment(data, pos, limit, true, false)
    }


    fun unsharedCopy(): Segment {
        return Segment(data.clone(), pos, limit, false, true)
    }

    fun pop(): Segment? {
        val result = if (next != this) next else null
        prev?.next = next
        next?.prev = prev
        next = null
        prev = null
        return result
    }

    fun push(segment: Segment): Segment {
        segment.prev = this
        segment.next = next
        next?.prev = segment
        next = segment
        return segment
    }

    fun split(byteCount: Int): Segment {
        require(!(byteCount <= 0 || byteCount > limit - pos))
        val prefix: Segment

        // We have two competing performance goals:
        //  - Avoid copying data. We accomplish this by sharing segments.
        //  - Avoid short shared segments. These are bad for performance because they are readonly and
        //    may lead to long chains of short segments.
        // To balance these goals we only share segments when the copy will be large.
        if (byteCount >= SHARE_MINIMUM) {
            prefix = sharedCopy()
        } else {
            prefix = SegmentPool.take()
            System.arraycopy(data, pos, prefix.data, 0, byteCount)
        }

        prefix.limit = prefix.pos + byteCount
        pos += byteCount
        prev!!.push(prefix)
        return prefix
    }

    fun compact() {
        check(prev != this)
        if (!prev!!.owner) return  // Cannot compact: prev isn't writable.
        val byteCount = limit - pos
        val availableByteCount = SIZE - prev!!.limit + if (prev!!.shared) 0 else prev!!.pos
        if (byteCount > availableByteCount) return  // Cannot compact: not enough writable space.
        writeTo(prev!!, byteCount)
        pop()
        SegmentPool.recycle(this)
    }

     fun writeTo(sink: Segment, byteCount: Int) {
        require(sink.owner)
        if (sink.limit + byteCount > SIZE) {
            // We can't fit byteCount bytes at the sink's current position. Shift sink first.
            require(!sink.shared)
            require(sink.limit + byteCount - sink.pos <= SIZE)
            System.arraycopy(sink.data, sink.pos, sink.data, 0, sink.limit - sink.pos)
            sink.limit -= sink.pos
            sink.pos = 0
        }

        System.arraycopy(data, pos, sink.data, sink.limit, byteCount)
        sink.limit += byteCount
        pos += byteCount
    }


}