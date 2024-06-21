package com.chat.compose.app.ui

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

/**
 * @author liuzhongao
 * @since 2024/6/21 14:01
 */

private const val AnimationDuration = 400
private const val FadeAnimationDuration = 300

private val enterTransition = slideInHorizontally(animationSpec = tween(AnimationDuration)) { it }
private val exitTransition = slideOutHorizontally(animationSpec = tween(AnimationDuration)) { it }
private val popEnterTransition = EnterTransition.None
private val popExitTransition = ExitTransition.None


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