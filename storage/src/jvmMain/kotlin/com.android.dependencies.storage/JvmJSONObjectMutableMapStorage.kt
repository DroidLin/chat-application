package com.android.dependencies.storage

import kotlin.concurrent.write

/**
 * @author liuzhongao
 * @since 2024/4/1 23:08
 */
internal class JvmJSONObjectMutableMapStorage(
    filePath: String,
    fileName: String
) : JvmJSONObjectMapStorage(filePath, fileName), MutableMapStorage {

    override fun putInt(key: String, value: Int) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putLong(key: String, value: Long) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putFloat(key: String, value: Float) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putDouble(key: String, value: Double) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putBoolean(key: String, value: Boolean) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putByte(key: String, value: Byte) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putChar(key: String, value: Char) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putShort(key: String, value: Short) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun putString(key: String, value: String?) = keepLatest {
        this.reentrantLock.write {
            this.innerMutableMap[key] = value
        }
    }

    override fun flush() {
        this.flushIntoFileSystem()
    }
}