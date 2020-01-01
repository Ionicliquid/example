package com.example.example.okhttp.okio

import okio.Timeout
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.channels.ByteChannel
import java.nio.charset.Charset

class Buffer : BufferedSource, BufferedSink, Cloneable, ByteChannel {


    private val DIGITS = byteArrayOf(
        '0'.toByte(),
        '1'.toByte(),
        '2'.toByte(),
        '3'.toByte(),
        '4'.toByte(),
        '5'.toByte(),
        '6'.toByte(),
        '7'.toByte(),
        '8'.toByte(),
        '9'.toByte(),
        'a'.toByte(),
        'b'.toByte(),
        'c'.toByte(),
        'd'.toByte(),
        'e'.toByte(),
        'f'.toByte()
    )
    internal val REPLACEMENT_CHARACTER: Int = '\ufffd'.toInt()


    var head: Segment? = null
    var size = 0L


    override fun write(byteString: ByteString): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(source: ByteArray): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(source: ByteArray, offset: Int, byteCount: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeAll(source: Source): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(source: Source, byteCount: Long): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeUtf8(string: String): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeUtf8(string: String, beginIndex: Int, endIndex: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeUtf8CodePoint(codePoint: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeString(string: String, charset: Charset): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeString(
        string: String,
        beginIndex: Int,
        endIndex: Int,
        charset: Charset
    ): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeByte(b: Int): BufferedSink {
        val tail = writableSegment(1)
        tail.data[tail.limit++] = b.toByte()
        size += 1
        return this
    }

    override fun writeShort(s: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeShortLe(s: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeInt(i: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeIntLe(i: Int): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeLong(v: Long): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeLongLe(v: Long): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeDecimalLong(v: Long): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeHexadecimalUnsignedLong(v: Long): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emit(): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun emitCompleteSegments(): BufferedSink {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun outputStream(): OutputStream {
        return object : OutputStream() {
            override fun write(b: Int) {
                writeByte(b.toByte().toInt())
            }

            override fun write(data: ByteArray, offset: Int, byteCount: Int) {
                this@Buffer.write(data, offset, byteCount)
            }

            override fun flush() {}

            override fun close() {}

            override fun toString(): String {
                return this@Buffer.toString() + ".outputStream()"
            }
        }
    }

    override fun read(sink: Buffer, byteCount: Long): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun timeout(): Timeout {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buffer(): Buffer {
        return this
    }

    override fun exhausted(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun require(byteCount: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun request(byteCount: Long): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readByte(): Byte {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readShort(): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readShortLe(): Short {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readInt(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readIntLe(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readLong(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readLongLe(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readDecimalLong(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readHexadecimalUnsignedLong(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun skip(byteCount: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readByteString(): ByteString {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun select(options: Options): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readByteArray(): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readByteArray(byteCount: Long): ByteArray {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(sink: ByteArray): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun read(dst: ByteBuffer?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isOpen(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun flush() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun write(src: ByteBuffer?): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun writableSegment(minimumCapacity: Int): Segment {
        require(!(minimumCapacity < 1 || minimumCapacity > Segment.SIZE))
        if (head == null) {
            head = SegmentPool.take()
            head!!.run {
                this.next = head
                this.prev = head
            }
            return head!!.next!!
        }
        var tail = head!!.prev!!
        if (tail.limit + minimumCapacity > Segment.SIZE || !tail.owner) {
            tail = tail.push(SegmentPool.take())

        }
        return tail


    }
}