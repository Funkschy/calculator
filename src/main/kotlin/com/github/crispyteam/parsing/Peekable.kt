package com.github.crispyteam.parsing

class Peekable<T>(private val iter: Iterator<T?>) : Iterator<T> {
    private var peeked: T? = null

    override fun next(): T =
        peeked?.let { value ->
            peeked = null
            value
        } ?: if (iter.hasNext())
            iter.next() ?: throw IllegalStateException("Calling next on empty iterator")
        else
            throw IllegalStateException("Calling next on empty iterator")

    override fun hasNext() = iter.hasNext() || peeked != null

    fun peek(): T? {
        if (peeked == null) {
            peeked = iter.next()
        }

        return peeked
    }
}

