package com.example.example.okhttp.okio

import okio.Timeout
import java.io.Closeable
import java.io.Flushable
import java.io.IOException
import java.io.OutputStream
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import java.nio.charset.Charset

interface Sink : Closeable, Flushable {


    @Throws(IOException::class)
    fun read(sink: ByteArray): Int

    @Throws(IOException::class)
    override fun flush()

    fun timeout(): Timeout

    @Throws(IOException::class)
    override fun close()

}

/**
 * Supplies a stream of bytes : 提供一个字节流
 */

interface Source : Closeable {

    @Throws(IOException::class)
    fun read(sink: Buffer, byteCount: Long): Long

    fun timeout(): Timeout

    @Throws(IOException::class)
    override fun close()
}

interface BufferedSource : Source, ReadableByteChannel {


    fun buffer(): Buffer

    @Throws(IOException::class)
    fun exhausted(): Boolean

    @Throws(IOException::class)
    fun require(byteCount: Long)

    @Throws(IOException::class)
    fun request(byteCount: Long): Boolean

    @Throws(IOException::class)
    fun readByte(): Byte

    @Throws(IOException::class)
    fun readShort(): Short

    @Throws(IOException::class)
    fun readShortLe(): Short

    @Throws(IOException::class)
    fun readInt(): Int

    @Throws(IOException::class)
    fun readIntLe(): Int

    @Throws(IOException::class)
    fun readLong(): Long


    @Throws(IOException::class)
    fun readLongLe(): Long

    @Throws(IOException::class)
    fun readDecimalLong(): Long


    @Throws(IOException::class)
    fun readHexadecimalUnsignedLong(): Long

    @Throws(IOException::class)
    fun skip(byteCount: Long)

    @Throws(IOException::class)
    fun readByteString(): ByteString

    @Throws(IOException::class)
    fun select(options: Options): Int


    @Throws(IOException::class)
    fun readByteArray(): ByteArray

    @Throws(IOException::class)
    fun readByteArray(byteCount: Long): ByteArray

    @Throws(IOException::class)
    fun read(sink: ByteArray): Int

}

interface BufferedSink : Sink, WritableByteChannel {
    /** Returns this sink's internal buffer.  */
    fun buffer(): Buffer

    @Throws(IOException::class)
    fun write(byteString: ByteString): BufferedSink

    /**
     * Like [OutputStream.write], this writes a complete byte array to
     * this sink.
     */
    @Throws(IOException::class)
    fun write(source: ByteArray): BufferedSink

    /**
     * Like [OutputStream.write], this writes `byteCount`
     * bytes of `source`, starting at `offset`.
     */
    @Throws(IOException::class)
    fun write(source: ByteArray, offset: Int, byteCount: Int): BufferedSink

    /**
     * Removes all bytes from `source` and appends them to this sink. Returns the
     * number of bytes read which will be 0 if `source` is exhausted.
     */
    @Throws(IOException::class)
    fun writeAll(source: Source): Long

    /** Removes `byteCount` bytes from `source` and appends them to this sink.  */
    @Throws(IOException::class)
    fun write(source: Source, byteCount: Long): BufferedSink

    /**
     * Encodes `string` in UTF-8 and writes it to this sink. <pre>`Buffer buffer = new Buffer();
     * buffer.writeUtf8("Uh uh uh!");
     * buffer.writeByte(' ');
     * buffer.writeUtf8("You didn't say the magic word!");
     *
     * assertEquals("Uh uh uh! You didn't say the magic word!", buffer.readUtf8());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeUtf8(string: String): BufferedSink

    /**
     * Encodes the characters at `beginIndex` up to `endIndex` from `string` in
     * UTF-8 and writes it to this sink. <pre>`Buffer buffer = new Buffer();
     * buffer.writeUtf8("I'm a hacker!\n", 6, 12);
     * buffer.writeByte(' ');
     * buffer.writeUtf8("That's what I said: you're a nerd.\n", 29, 33);
     * buffer.writeByte(' ');
     * buffer.writeUtf8("I prefer to be called a hacker!\n", 24, 31);
     *
     * assertEquals("hacker nerd hacker!", buffer.readUtf8());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeUtf8(string: String, beginIndex: Int, endIndex: Int): BufferedSink

    /** Encodes `codePoint` in UTF-8 and writes it to this sink.  */
    @Throws(IOException::class)
    fun writeUtf8CodePoint(codePoint: Int): BufferedSink

    /** Encodes `string` in `charset` and writes it to this sink.  */
    @Throws(IOException::class)
    fun writeString(string: String, charset: Charset): BufferedSink

    /**
     * Encodes the characters at `beginIndex` up to `endIndex` from `string` in
     * `charset` and writes it to this sink.
     */
    @Throws(IOException::class)
    fun writeString(
        string: String,
        beginIndex: Int,
        endIndex: Int,
        charset: Charset
    ): BufferedSink

    /** Writes a byte to this sink.  */
    @Throws(IOException::class)
    fun writeByte(b: Int): BufferedSink

    /**
     * Writes a big-endian short to this sink using two bytes. <pre>`Buffer buffer = new Buffer();
     * buffer.writeShort(32767);
     * buffer.writeShort(15);
     *
     * assertEquals(4, buffer.size());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeShort(s: Int): BufferedSink

    /**
     * Writes a little-endian short to this sink using two bytes. <pre>`Buffer buffer = new Buffer();
     * buffer.writeShortLe(32767);
     * buffer.writeShortLe(15);
     *
     * assertEquals(4, buffer.size());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeShortLe(s: Int): BufferedSink

    /**
     * Writes a big-endian int to this sink using four bytes. <pre>`Buffer buffer = new Buffer();
     * buffer.writeInt(2147483647);
     * buffer.writeInt(15);
     *
     * assertEquals(8, buffer.size());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeInt(i: Int): BufferedSink

    /**
     * Writes a little-endian int to this sink using four bytes.  <pre>`Buffer buffer = new Buffer();
     * buffer.writeIntLe(2147483647);
     * buffer.writeIntLe(15);
     *
     * assertEquals(8, buffer.size());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeIntLe(i: Int): BufferedSink

    /**
     * Writes a big-endian long to this sink using eight bytes. <pre>`Buffer buffer = new Buffer();
     * buffer.writeLong(9223372036854775807L);
     * buffer.writeLong(15);
     *
     * assertEquals(16, buffer.size());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeLong(v: Long): BufferedSink

    /**
     * Writes a little-endian long to this sink using eight bytes. <pre>`Buffer buffer = new Buffer();
     * buffer.writeLongLe(9223372036854775807L);
     * buffer.writeLongLe(15);
     *
     * assertEquals(16, buffer.size());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0xff, buffer.readByte());
     * assertEquals((byte) 0x7f, buffer.readByte());
     * assertEquals((byte) 0x0f, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals((byte) 0x00, buffer.readByte());
     * assertEquals(0, buffer.size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeLongLe(v: Long): BufferedSink

    /**
     * Writes a long to this sink in signed decimal form (i.e., as a string in base 10). <pre>`Buffer buffer = new Buffer();
     * buffer.writeDecimalLong(8675309L);
     * buffer.writeByte(' ');
     * buffer.writeDecimalLong(-123L);
     * buffer.writeByte(' ');
     * buffer.writeDecimalLong(1L);
     *
     * assertEquals("8675309 -123 1", buffer.readUtf8());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeDecimalLong(v: Long): BufferedSink

    /**
     * Writes a long to this sink in hexadecimal form (i.e., as a string in base 16). <pre>`Buffer buffer = new Buffer();
     * buffer.writeHexadecimalUnsignedLong(65535L);
     * buffer.writeByte(' ');
     * buffer.writeHexadecimalUnsignedLong(0xcafebabeL);
     * buffer.writeByte(' ');
     * buffer.writeHexadecimalUnsignedLong(0x10L);
     *
     * assertEquals("ffff cafebabe 10", buffer.readUtf8());
    `</pre> *
     */
    @Throws(IOException::class)
    fun writeHexadecimalUnsignedLong(v: Long): BufferedSink

    /**
     * Writes all buffered data to the underlying sink, if one exists. Then that sink is recursively
     * flushed which pushes data as far as possible towards its ultimate destination. Typically that
     * destination is a network socket or file. <pre>`BufferedSink b0 = new Buffer();
     * BufferedSink b1 = Okio.buffer(b0);
     * BufferedSink b2 = Okio.buffer(b1);
     *
     * b2.writeUtf8("hello");
     * assertEquals(5, b2.buffer().size());
     * assertEquals(0, b1.buffer().size());
     * assertEquals(0, b0.buffer().size());
     *
     * b2.flush();
     * assertEquals(0, b2.buffer().size());
     * assertEquals(0, b1.buffer().size());
     * assertEquals(5, b0.buffer().size());
    `</pre> *
     */
    @Throws(IOException::class)
    override fun flush()

    /**
     * Writes all buffered data to the underlying sink, if one exists. Like [.flush], but
     * weaker. Call this before this buffered sink goes out of scope so that its data can reach its
     * destination. <pre>`BufferedSink b0 = new Buffer();
     * BufferedSink b1 = Okio.buffer(b0);
     * BufferedSink b2 = Okio.buffer(b1);
     *
     * b2.writeUtf8("hello");
     * assertEquals(5, b2.buffer().size());
     * assertEquals(0, b1.buffer().size());
     * assertEquals(0, b0.buffer().size());
     *
     * b2.emit();
     * assertEquals(0, b2.buffer().size());
     * assertEquals(5, b1.buffer().size());
     * assertEquals(0, b0.buffer().size());
     *
     * b1.emit();
     * assertEquals(0, b2.buffer().size());
     * assertEquals(0, b1.buffer().size());
     * assertEquals(5, b0.buffer().size());
    `</pre> *
     */
    @Throws(IOException::class)
    fun emit(): BufferedSink

    /**
     * Writes complete segments to the underlying sink, if one exists. Like [.flush], but
     * weaker. Use this to limit the memory held in the buffer to a single segment. Typically
     * application code will not need to call this: it is only necessary when application code writes
     * directly to this [sink&#39;s buffer][.buffer]. <pre>`BufferedSink b0 = new Buffer();
     * BufferedSink b1 = Okio.buffer(b0);
     * BufferedSink b2 = Okio.buffer(b1);
     *
     * b2.buffer().write(new byte[20_000]);
     * assertEquals(20_000, b2.buffer().size());
     * assertEquals(     0, b1.buffer().size());
     * assertEquals(     0, b0.buffer().size());
     *
     * b2.emitCompleteSegments();
     * assertEquals( 3_616, b2.buffer().size());
     * assertEquals(     0, b1.buffer().size());
     * assertEquals(16_384, b0.buffer().size()); // This example assumes 8192 byte segments.
    `</pre> *
     */
    @Throws(IOException::class)
    fun emitCompleteSegments(): BufferedSink

    /** Returns an output stream that writes to this sink.  */
    fun outputStream(): OutputStream
}