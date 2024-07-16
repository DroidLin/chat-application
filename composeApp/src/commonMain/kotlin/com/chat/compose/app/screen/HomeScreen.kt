package com.chat.compose.app.screen

import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import androidx.window.core.layout.WindowSizeClass
import com.chat.compose.app.screen.message.ui.SessionDetailScreen
import com.chat.compose.app.screen.message.ui.SessionListScreen
import com.chat.compose.app.screen.search.SearchLauncherScreen
import com.chat.compose.app.screen.search.SearchResultScreen
import com.chat.compose.app.screen.user.UserBasicInfoScreen
import com.chat.compose.app.ui.AppBackHandler
import com.chat.compose.app.ui.windowAdaptiveInfo

/**
 * @author liuzhongao
 * @since 2024/7/16 00:26
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen() {
    val navigator = rememberListDetailPaneScaffoldNavigator<NavigatorMetadata>(
        scaffoldDirective = calculatePaneScaffoldDirective(windowAdaptiveInfo()),
        isDestinationHistoryAware = false,
    )
    val contentValueState = remember {
        derivedStateOf {
            navigator.currentDestination
        }
    }
    val onBackPress: () -> Unit = {
        navigator.navigateBack(BackNavigationBehavior.PopLatest)
    }
    AppBackHandler(navigator.canNavigateBack(), onBackPress)
    LaunchedEffect(navigator) { navigator.navigateTo(ThreePaneScaffoldRole.Secondary, ListMetadata.SessionList) }
    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            var currentDestination by remember { mutableStateOf<ListMetadata?>(null) }
            LaunchedEffect(contentValueState.value) {
                val content = contentValueState.value
                val navigatorMetadata = content?.content
                if (content?.pane == ThreePaneScaffoldRole.Secondary && navigatorMetadata is ListMetadata) {
                    currentDestination = navigatorMetadata
                }
            }
            AnimatedPane {
                Surface(
                    modifier = Modifier.animateEnterExit()
                ) {
                    when (val content = currentDestination) {
                        ListMetadata.SessionList -> {
                            SessionListScreen(
                                backPress = onBackPress,
                                sessionItemClick = { sessionContact ->
                                    val meta = DetailMetadata.SessionDetailMetadata(
                                        sessionId = sessionContact.sessionId,
                                        sessionType = sessionContact.sessionType,
                                    )
                                    navigator.navigateTo(ThreePaneScaffoldRole.Primary, meta)
                                },
                                navigateToSearch = {
                                    navigator.navigateTo(ThreePaneScaffoldRole.Tertiary, ThirdPartyMetadata.SearchLauncher)
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        },
        detailPane = {
            var currentDestination by remember { mutableStateOf<DetailMetadata?>(null) }
            LaunchedEffect(contentValueState.value) {
                val content = contentValueState.value
                val navigatorMetadata = content?.content
                if (content?.pane == ThreePaneScaffoldRole.Primary && navigatorMetadata is DetailMetadata) {
                    currentDestination = navigatorMetadata
                }
            }
            AnimatedPane {
                Surface(
                    modifier = Modifier.animateEnterExit()
                ) {
                    when (val content = currentDestination) {
                        is DetailMetadata.SessionDetailMetadata -> {
                            SessionDetailScreen(
                                sessionId = content.sessionId,
                                sessionType = content.sessionType,
                                backPress = onBackPress,
                                navigateToUserBasicInfo = { userId ->
                                    navigator.navigateTo(
                                        ThreePaneScaffoldRole.Tertiary,
                                        ThirdPartyMetadata.UserBasicInfoMeta(userId)
                                    )
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        },
        extraPane = {
            var currentDestination by remember { mutableStateOf<ThirdPartyMetadata?>(null) }
            LaunchedEffect(contentValueState.value) {
                val content = contentValueState.value
                val navigatorMetadata = content?.content
                if (content?.pane == ThreePaneScaffoldRole.Tertiary && navigatorMetadata is ThirdPartyMetadata) {
                    currentDestination = navigatorMetadata
                }
            }
            AnimatedPane {
                Surface(
                    modifier = Modifier.animateEnterExit()
                ) {
                    when (val content = currentDestination) {
                        is ThirdPartyMetadata.UserBasicInfoMeta -> {
                            UserBasicInfoScreen(
                                userId = content.userId,
                                backPress = onBackPress,
                                navigateToChat = { sessionId, sessionType ->
                                    val meta = DetailMetadata.SessionDetailMetadata(
                                        sessionId = sessionId,
                                        sessionType = sessionType,
                                    )
                                    navigator.navigateTo(ThreePaneScaffoldRole.Primary, meta)
                                }
                            )
                        }

                        is ThirdPartyMetadata.SearchLauncher -> {
                            SearchLauncherScreen(
                                backPressed = onBackPress,
                                navigateToSearchResult = { keyword ->
                                    navigator.navigateTo(ThreePaneScaffoldRole.Tertiary, ThirdPartyMetadata.SearchResult(keyword))
                                }
                            )
                        }

                        is ThirdPartyMetadata.SearchResult -> {
                            SearchResultScreen(
                                keyword = content.keyword,
                                backPressed = onBackPress,
                                navigateToUseBasicScreen = { userId ->
                                    navigator.navigateTo(
                                        ThreePaneScaffoldRole.Tertiary,
                                        ThirdPartyMetadata.UserBasicInfoMeta(userId)
                                    )
                                }
                            )
                        }

                        else -> {}
                    }
                }
            }
        }
    )
}
