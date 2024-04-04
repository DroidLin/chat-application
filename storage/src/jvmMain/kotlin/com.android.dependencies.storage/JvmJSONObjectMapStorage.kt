package com.android.dependencies.storage

import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * @author liuzhongao
 * @since 2024/3/29 00:11
 */
internal open class JvmJSONObjectMapStorage(
    filePath: String,
    fileName: String
) : MapStorage {

    private val fileSystemFile: File = File(filePath, fileName)
    private val randomAccessFile = RandomAccessFile(this.fileSystemFile, "rw")

    protected val innerMutableMap = HashMap<String, Any?>()
    protected val reentrantLock = ReentrantReadWriteLock()

    private var lastFileModifiedTimestamp: Long = fileSystemFile.lastModified()

    override val absolutePath: String = filePath
    override val storageName: String = fileName

    override val byteArray: ByteArray
        get() = this.readByteArray()
    override val all: Map<String, Any?>
        get() {
            this.trySyncStorageFromFileSystem()
            return this.innerMutableMap.toMap()
        }

    override fun contains(key: String): Boolean {
        this.trySyncStorageFromFileSystem()
        return this.reentrantLock.write {
            this.innerMutableMap[key] != null
        }
    }

    override fun getInt(key: String, default: Int): Int = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Int ?: default
        }
    }

    override fun getLong(key: String, default: Long): Long = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Long ?: default
        }
    }

    override fun getFloat(key: String, default: Float): Float = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Float ?: default
        }
    }

    override fun getDouble(key: String, default: Double): Double = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Double ?: default
        }
    }

    override fun getBoolean(key: String, default: Boolean): Boolean = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Boolean ?: default
        }
    }

    override fun getByte(key: String, default: Byte): Byte = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Byte ?: default
        }
    }

    override fun getChar(key: String, default: Char): Char = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Char ?: default
        }
    }

    override fun getShort(key: String, default: Short): Short = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? Short ?: default
        }
    }

    override fun getString(key: String, default: String?): String? = keepLatest {
        this.reentrantLock.read {
            this.innerMutableMap[key] as? String ?: default
        }
    }

    protected inline fun <T> keepLatest(function: () -> T): T {
        this.trySyncStorageFromFileSystem()
        return function()
    }

    protected fun flushIntoFileSystem() {
        this.reentrantLock.write {
            this.tryCreateFileAndDirectories(this.fileSystemFile)
            FileOutputStream(this.fileSystemFile).use { fileOutputStream ->
                this.randomAccessFile.channel.lock().use { _ ->
                    val jsonMapStringContent = JSONObject(this.innerMutableMap).toString()
                    fileOutputStream.write(jsonMapStringContent.toByteArray())
                    fileOutputStream.flush()
                }
            }
        }
    }

    private fun trySyncStorageFromFileSystem() {
        val lastModifiedTimestamp = this.fileSystemFile.lastModified()
        if (lastModifiedTimestamp != this.lastFileModifiedTimestamp) {
            this.syncStorageDataFromFileSystem()
            this.lastFileModifiedTimestamp = lastModifiedTimestamp
        }
    }

    private fun syncStorageDataFromFileSystem() {
        val byteArrayInFile = this.reentrantLock.read {
            this.readByteArray()
        }
        val contentString = String(byteArrayInFile)
        kotlin.runCatching {
            JSONObject(contentString)
        }.onSuccess { jsonObject ->
            this.reentrantLock.write {
                val lastMutableMap = this.innerMutableMap.toMap()
                this.innerMutableMap.clear()
                this.innerMutableMap.putAll(jsonObject.toMap())
                // we assume values in current storage is the latest.
                this.innerMutableMap.putAll(lastMutableMap)
            }
        }.onFailure { it.printStackTrace() }
    }

    /**
     * may block current thread execution.
     */
    private fun readByteArray(): ByteArray {
        if (!this.fileSystemFile.exists()) {
            return ByteArray(0)
        }
        return FileInputStream(this.fileSystemFile).use { fileInputStream ->
            this.randomAccessFile.channel.lock().use { _ ->
                val byteArray = ByteArray(1024)
                val byteArrayOutputStream = ByteArrayOutputStream()
                var bytesRead = 0
                while (fileInputStream.read(byteArray).also { bytesRead = it } != -1) {
                    byteArrayOutputStream.write(byteArray, 0, bytesRead)
                }
                byteArrayOutputStream.toByteArray()
            }
        }
    }

    private fun tryCreateFileAndDirectories(file: File) {
        val directoryFile = file.parentFile
        if (!directoryFile.exists() && !directoryFile.mkdirs()) {
            throw RuntimeException("cannot create directory for path: ${directoryFile.absolutePath}")
        }
        if (!file.exists() && !file.createNewFile()) {
            throw RuntimeException("cannot create new file for path: ${file.absolutePath}")
        }
    }
}