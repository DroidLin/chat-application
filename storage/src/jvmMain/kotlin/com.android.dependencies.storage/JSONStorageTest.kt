package com.android.dependencies.storage

import java.util.concurrent.Executors

/**
 * @author liuzhongao
 * @since 2024/4/2 23:22
 */
fun main() {
    val executor = Executors.newFixedThreadPool(4)
    val storage = MutableMapStorage("/Users/liuzhongao", "test.txt")

    val future1 = executor.submit {
        val startTimestamp = System.currentTimeMillis()
        for (index in 0 until 100) {
            storage.putInt("key_extra_${index}", index)
        }
        storage.flush()
        println("future1 cost: ${System.currentTimeMillis() - startTimestamp}ms")
    }
    val future2 = executor.submit {
        val startTimestamp = System.currentTimeMillis()
        for (index in 100 until 200) {
            storage.putInt("key_extra_${index}", index)
        }
        storage.flush()
        println("future2 cost: ${System.currentTimeMillis() - startTimestamp}ms")
    }

    future1.get()
    future2.get()
    executor.shutdown()
}