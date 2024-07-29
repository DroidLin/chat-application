package com.chat.compose.app.ui

import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.lazy.LazyListState

/**
 * @author liuzhongao
 * @since 2024/7/28 23:32
 */
suspend fun LazyListState.fastScrollToPosition(position: Int, threshold: Int = 5) {
    if (this.layoutInfo.visibleItemsInfo.isEmpty()) return

    val firstVisibleIndex = this.layoutInfo.visibleItemsInfo.first().index
    val lastVisibleIndex = this.layoutInfo.visibleItemsInfo.last().index

    this.stopScroll()
    if (position < firstVisibleIndex - threshold) {
        this.scrollToItem(position + threshold)
        this.animateScrollToItem(position)
    } else if (firstVisibleIndex - threshold <= position && position <= lastVisibleIndex + threshold) {
        this.animateScrollToItem(position)
    } else {
        this.scrollToItem(position - threshold)
        this.animateScrollToItem(position)
    }
}