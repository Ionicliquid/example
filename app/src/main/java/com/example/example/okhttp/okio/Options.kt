package com.example.example.okhttp.okio

class Options : AbstractList<ByteString>(), RandomAccess {


    override val size: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun equals(other: Any?): Boolean {
        return super<AbstractList>.equals(other)
    }

    override fun get(index: Int): ByteString {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hashCode(): Int {
        return super<AbstractList>.hashCode()
    }

    override fun indexOf(element: ByteString): Int {
        return super.indexOf(element)
    }

    override fun iterator(): Iterator<ByteString> {
        return super.iterator()
    }

    override fun lastIndexOf(element: ByteString): Int {
        return super.lastIndexOf(element)
    }

    override fun listIterator(): ListIterator<ByteString> {
        return super.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<ByteString> {
        return super.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<ByteString> {
        return super.subList(fromIndex, toIndex)
    }
}