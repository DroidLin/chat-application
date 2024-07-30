package com.chat.compose.app.ui.overscroll

import androidx.compose.animation.core.*
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.fastForEachIndexed
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs
import kotlin.math.max

/**
 * @author liuzhongao
 * @since 2024/4/22 23:36
 */
@Composable
fun rememberOverScrollState(
    overScrollSize: Dp = 200.dp,
    orientation: OverScrollableOrientation = OverScrollableOrientation.Vertical
): OverScrollableState {
    val density = LocalDensity.current
    return rememberSaveable(
        inputs = arrayOf(overScrollSize, density, orientation),
        saver = OverScrollableState.Saver
    ) {
        OverScrollableState(
            initialScrollOffsetLimit = density.run { overScrollSize.toPx() },
            orientation = orientation
        )
    }
}

@Composable
fun rememberOverScrollConnection(
    state: OverScrollableState = rememberOverScrollState(),
    flingAnimationSpec: DecayAnimationSpec<Float>? = rememberSplineBasedDecay(),
    restoreToDefaultAnimationSpec: AnimationSpec<Float> = remember { spring(stiffness = Spring.StiffnessLow) },
    canOverScroll: () -> Boolean = { true }
): OverScrollableNestedScrollConnection {
    return remember(state, flingAnimationSpec, canOverScroll) {
        OverScrollableNestedScrollConnection(
            overScrollableState = state,
            flingAnimationSpec = flingAnimationSpec,
            canOverScroll = canOverScroll,
            restoreToDefaultAnimationSpec = restoreToDefaultAnimationSpec
        )
    }
}

@Composable
fun OverScrollableContainer(
    modifier: Modifier = Modifier,
    state: OverScrollableState = rememberOverScrollState(),
    content: @Composable () -> Unit
) {
//    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
//    }
    Layout(
        modifier = modifier.clipToBounds(),
        content = content,
        measurePolicy = { measurables: List<Measurable>, constraints: Constraints ->
            if (measurables.isEmpty()) {
                return@Layout layout(constraints.minWidth, constraints.minHeight) {}
            }

            val placeables = arrayOfNulls<Placeable>(measurables.size)
            // First measure non match parent size children to get the size of the Box.
            var boxWidth = constraints.minWidth
            var boxHeight = constraints.minHeight
            measurables.fastForEachIndexed { index, measurable ->
                val placeable = measurable.measure(constraints)
                placeables[index] = placeable
                boxWidth = max(boxWidth, placeable.width)
                boxHeight = max(boxHeight, placeable.height)
            }

            // Specify the size of the Box and position its children.
            return@Layout layout(boxWidth, boxHeight) {
                placeables.forEachIndexed { _, placeable ->
                    placeInBox(
                        placeable = placeable as Placeable,
                        layoutDirection = layoutDirection,
                        boxWidth = boxWidth,
                        boxHeight = boxHeight,
                        yOffset = state.scrollOffset.toInt(),
                        orientation = state.orientation
                    )
                }
            }
        }
    )
}

private fun Placeable.PlacementScope.placeInBox(
    placeable: Placeable,
    layoutDirection: LayoutDirection,
    boxWidth: Int,
    boxHeight: Int,
    yOffset: Int,
    orientation: OverScrollableOrientation,
) {
    val position = Alignment.Center.align(
        size = IntSize(placeable.width, placeable.height),
        space = IntSize(boxWidth, boxHeight),
        layoutDirection = layoutDirection
    ).run {
        when (orientation) {
            OverScrollableOrientation.Vertical -> copy(y = this.y + yOffset)
            OverScrollableOrientation.Horizontal -> copy(x = this.x + yOffset)
            else -> this
        }
    }
    placeable.place(position)
}

@Stable
enum class OverScrollableOrientation {
    Vertical,
    Horizontal;
}

@Stable
class OverScrollableState(
    internal val initialScrollOffsetLimit: Float,
    internal val orientation: OverScrollableOrientation = OverScrollableOrientation.Vertical,
) {
    private var _scrollOffset by mutableFloatStateOf(0f)
    var scrollOffset: Float
        set(value) {
            this._scrollOffset =
                value.coerceIn(-this.initialScrollOffsetLimit, this.initialScrollOffsetLimit)
        }
        get() = this._scrollOffset

    companion object {
        val Saver = listSaver<OverScrollableState, Any>(
            save = { state ->
                listOf(state.initialScrollOffsetLimit, state.orientation.name, state.scrollOffset)
            },
            restore = { value ->
                val initialScrollOffsetLimit = value[0] as Float
                val orientation = value[1] as String
                val scrollOffset = value[2] as Float

                val overScrollableState = OverScrollableState(
                    initialScrollOffsetLimit = initialScrollOffsetLimit,
                    orientation = OverScrollableOrientation.valueOf(orientation)
                )
                overScrollableState.scrollOffset = scrollOffset
                overScrollableState
            }
        )
    }
}

@Stable
class OverScrollableNestedScrollConnection(
    val overScrollableState: OverScrollableState,
    internal val flingAnimationSpec: DecayAnimationSpec<Float>?,
    internal val restoreToDefaultAnimationSpec: AnimationSpec<Float>,
    internal val canOverScroll: () -> Boolean
) : NestedScrollConnection {

    private val atomicInteger = AtomicInteger()

    private val initialOffsetLimit: Float get() = this.overScrollableState.initialScrollOffsetLimit
    private val orientation: OverScrollableOrientation get() = this.overScrollableState.orientation

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (!this.canOverScroll()) return Offset.Zero
        val offsetBeforeChange = this.overScrollableState.scrollOffset

        val nowIntegerValue = this.atomicInteger.get()
        this.atomicInteger.compareAndSet(nowIntegerValue, 0)
        if (this.orientation == OverScrollableOrientation.Vertical) {
            if (available.y < 0) { // scroll down
                if (offsetBeforeChange > 0) {
                    val deltaConsume = if (offsetBeforeChange + available.y < 0) {
                        -offsetBeforeChange
                    } else available.y
                    this.overScrollableState.scrollOffset += deltaConsume
                    return available.copy(x = 0f, y = this.overScrollableState.scrollOffset - offsetBeforeChange)
                }
            } else if (available.y > 0) { // scroll up
                if (offsetBeforeChange < 0) {
                    val deltaConsume = if (offsetBeforeChange + available.y > 0) {
                        -offsetBeforeChange
                    } else available.y
                    this.overScrollableState.scrollOffset += deltaConsume
                    return available.copy(x = 0f, y = this.overScrollableState.scrollOffset - offsetBeforeChange)
                }
            }
        } else if (this.orientation == OverScrollableOrientation.Horizontal) {
            if (available.x < 0) { // scroll down
                if (offsetBeforeChange > 0) {
                    val deltaConsume = if (offsetBeforeChange + available.x < 0) {
                        -offsetBeforeChange
                    } else available.x
                    this.overScrollableState.scrollOffset += deltaConsume
                    return available.copy(y = 0f, x = this.overScrollableState.scrollOffset - offsetBeforeChange)
                }
            } else if (available.x > 0) { // scroll up
                if (offsetBeforeChange < 0) {
                    val deltaConsume = if (offsetBeforeChange + available.x > 0) {
                        -offsetBeforeChange
                    } else available.x
                    this.overScrollableState.scrollOffset += deltaConsume
                    return available.copy(y = 0f, x = this.overScrollableState.scrollOffset - offsetBeforeChange)
                }
            }
        }

        return super.onPreScroll(available, source)
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        if (!this.canOverScroll()) return Offset.Zero
        val nowIntegerValue = this.atomicInteger.get()
        this.atomicInteger.compareAndSet(nowIntegerValue, 0)
        val offsetBeforeChange = this.overScrollableState.scrollOffset
        val consumeDelta = available.consumeDelta(
            offsetBeforeChange = offsetBeforeChange,
            offsetLimit = this.initialOffsetLimit,
            orientation = this.orientation,
            source = source
        )
        this.overScrollableState.scrollOffset = offsetBeforeChange + consumeDelta
        return if (this.overScrollableState.scrollOffset != offsetBeforeChange) {
            val offsetAfterChange = this.overScrollableState.scrollOffset - offsetBeforeChange
            when (this.overScrollableState.orientation) {
                OverScrollableOrientation.Vertical -> available.copy(x = 0f, y = offsetAfterChange)
                OverScrollableOrientation.Horizontal -> available.copy(y = 0f, x = offsetAfterChange)
                else -> Offset.Zero
            }
        } else Offset.Zero
    }

    private fun Offset.consumeDelta(
        offsetBeforeChange: Float,
        offsetLimit: Float,
        orientation: OverScrollableOrientation,
        source: NestedScrollSource
    ): Float {
        val value = when (orientation) {
            OverScrollableOrientation.Vertical -> this.y
            OverScrollableOrientation.Horizontal -> this.x
            else -> 0f
        }
        val ratio = when (source) {
            NestedScrollSource.Drag -> 2f
            else -> 2f
        }
        // y = b * (1 - a * x)
        return value * ((offsetLimit - ratio * abs(offsetBeforeChange)) / offsetLimit)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        val superFlingConsumed = super.onPostFling(consumed, available)
        if (abs(this.overScrollableState.scrollOffset) > 0) {
            val token = this.atomicInteger.incrementAndGet()
            AnimationState(initialValue = this.overScrollableState.scrollOffset).animateTo(
                targetValue = 0f,
                animationSpec = this.restoreToDefaultAnimationSpec
            ) {
                val currentAtomicIntegerToken =
                    this@OverScrollableNestedScrollConnection.atomicInteger.get()
                if (token == currentAtomicIntegerToken) {
                    this@OverScrollableNestedScrollConnection.overScrollableState.scrollOffset =
                        this.value
                } else this.cancelAnimation()
            }
        }
        return superFlingConsumed + Velocity(x = 0f, available.y)
    }
}
