package com.example.example.util

import java.io.Serializable
import java.util.*
import java.util.concurrent.BlockingDeque
import java.util.concurrent.TimeUnit

class SynchronousQueue <E> :AbstractQueue<E>(),BlockingDeque<E>,Serializable{

    companion object{
        private const val serialVersionUID = -3223113410248163686L
    }

    override fun contains(element: E?): Boolean {
        return super.contains(element)
    }

    override fun toArray(): Array<Any> {
        return super.toArray()
    }

    override fun <T : Any?> toArray(a: Array<T>): Array<T> {
        return super.toArray(a)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        return super.addAll(elements)
    }

    override fun clear() {
        super.clear()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun element(): E {
        return super.element()
    }

    override fun isEmpty(): Boolean {
        return super.isEmpty()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }



    override fun remove(): E {
        return super.remove()
    }

    override fun remove(element: E?): Boolean {
        return super.remove(element)
    }

    override fun containsAll(elements: Collection<E>): Boolean {
        return super.containsAll(elements)
    }




    override fun removeAll(elements: Collection<E>): Boolean {
        return super.removeAll(elements)
    }

    override fun add(element: E): Boolean {
        return super.add(element)
    }

    override fun offer(e: E): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun offer(e: E, timeout: Long, unit: TimeUnit): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun iterator(): MutableIterator<E> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        return super.retainAll(elements)
    }



    override fun peek(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    override fun poll(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun poll(timeout: Long, unit: TimeUnit): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val size: Int
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun peekLast(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addFirst(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun offerLast(e: E): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun offerLast(e: E, timeout: Long, unit: TimeUnit?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putFirst(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFirst(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeLast(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun peekFirst(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeFirst(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun drainTo(c: MutableCollection<in E>): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun drainTo(c: MutableCollection<in E>, maxElements: Int): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun offerFirst(e: E): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun offerFirst(e: E, timeout: Long, unit: TimeUnit?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pollFirst(timeout: Long, unit: TimeUnit?): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pollFirst(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFirstOccurrence(o: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun descendingIterator(): MutableIterator<E> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun push(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun take(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLast(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addLast(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun putLast(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFirst(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun put(e: E) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLastOccurrence(o: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeLast(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pollLast(timeout: Long, unit: TimeUnit?): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pollLast(): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pop(): E {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remainingCapacity(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}