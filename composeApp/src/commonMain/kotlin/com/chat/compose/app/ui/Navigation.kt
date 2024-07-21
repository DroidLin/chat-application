package com.chat.compose.app.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * @author liuzhongao
 * @since 2024/6/21 14:01
 */

private const val AnimationDuration = 300
private const val FadeAnimationDuration = 300

private val animationSpec = spring(
    dampingRatio = 0.8f,
    stiffness = 600f,
    visibilityThreshold = IntOffset.VisibilityThreshold
)

private val enterTransition = slideInHorizontally(animationSpec = animationSpec) { it }
private val exitTransition = slideOutHorizontally(animationSpec = animationSpec) { -it }
private val popEnterTransition = slideInHorizontally(animationSpec = animationSpec) { -it }
private val popExitTransition = slideOutHorizontally(animationSpec = animationSpec) { it }

fun NavGraphBuilder.homeNavigationComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = { fadeIn(tween(FadeAnimationDuration)) },
        exitTransition = { fadeOut(tween(FadeAnimationDuration)) },
        popEnterTransition = { fadeIn(tween(FadeAnimationDuration)) },
        popExitTransition = { fadeOut(tween(FadeAnimationDuration)) },
        content = content
    )
}

fun NavGraphBuilder.navigationComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = { enterTransition },
        exitTransition = { exitTransition },
        popEnterTransition = { popEnterTransition },
        popExitTransition = { popExitTransition },
        content = content
    )
}