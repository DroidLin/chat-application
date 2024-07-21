package com.application.channel.message

import java.util.function.BiConsumer
import java.util.function.BiFunction

/**
 * @author liuzhongao
 * @since 2024/7/22 00:05
 */
interface AccessibleMutableMap<K, V> : MutableMap<K, V> {
    val isAccessed: Boolean
    val isModified: Boolean
}

fun <K, V> MutableMap<K, V>.accessibleMutableMap(): AccessibleMutableMap<K, V> {
    return AccessibleMutableMap(this)
}

fun <K, V> AccessibleMutableMap(mutableMap: MutableMap<K, V>): AccessibleMutableMap<K, V> {
    return AccessibleMutableMapImpl(mutableMap)
}

private class AccessibleMutableMapImpl<K, V> : HashMap<K, V>, AccessibleMutableMap<K, V>, MutableMap<K, V> {

    constructor(initialCapacity: Int, loadFactor: Float) : super(initialCapacity, loadFactor)
    constructor(initialCapacity: Int) : super(initialCapacity)
    constructor() : super()
    constructor(m: MutableMap<out K, out V>) : super(m)

    override var isAccessed: Boolean = false
    override var isModified: Boolean = false

    override val keys: MutableSet<K>
        get()  {
            this.isAccessed = true
            this.isModified = true
            return super.keys
        }

    override val values: MutableCollection<V>
        get() {
            this.isAccessed = true
            this.isModified = true
            return super.values
        }

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
        get() {
            this.isAccessed = true
            this.isModified = true
            return super.entries
        }

    override fun getOrDefault(key: K, defaultValue: V): V {
        this.isAccessed = true
        return super<AccessibleMutableMap>.getOrDefault(key, defaultValue)
    }

    override fun forEach(action: BiConsumer<in K, in V>) {
        this.isAccessed = true
        super<AccessibleMutableMap>.forEach(action)
    }

    override fun remove(key: K, value: V): Boolean {
        this.isModified = true
        return super<AccessibleMutableMap>.remove(key, value)
    }

    override fun replaceAll(function: BiFunction<in K, in V, out V>) {
        this.isModified = true
        super<AccessibleMutableMap>.replaceAll(function)
    }

    override fun putIfAbsent(key: K, value: V): V? {
        this.isModified = true
        return super<AccessibleMutableMap>.putIfAbsent(key, value)
    }

    override fun replace(key: K, oldValue: V, newValue: V): Boolean {
        this.isModified = true
        return super<AccessibleMutableMap>.replace(key, oldValue, newValue)
    }

    override fun replace(key: K, value: V): V? {
        this.isModified = true
        return super<AccessibleMutableMap>.replace(key, value)
    }

    override fun putAll(from: Map<out K, V>) {
        this.isModified = true
        super.putAll(from)
    }

    override fun merge(key: K, value: V & Any, remappingFunction: BiFunction<in V & Any, in V & Any, out V?>): V? {
        this.isAccessed = true
        this.isModified = true
        return super<AccessibleMutableMap>.merge(key, value, remappingFunction)
    }

    companion object {
        private const val serialVersionUID: Long = 97987869054172052L
    }
}