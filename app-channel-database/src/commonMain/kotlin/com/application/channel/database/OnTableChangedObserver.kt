package com.application.channel.database

/**
 * @author liuzhongao
 * @since 2024/6/11 23:28
 */
interface OnTableChangedObserver {
    val tables: List<String>
    fun onTableChanged(tableNames: Set<String>)
}