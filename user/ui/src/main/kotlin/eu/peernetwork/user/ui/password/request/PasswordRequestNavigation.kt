package eu.peernetwork.user.ui.password.request

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignRouter
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.design.material.DesignTitleBar
import eu.peernetwork.user.ui.password.reset.PasswordResetScreen

@Composable
fun PasswordRequestNavigation(
    controller: NavHostController,
    provider: UiComponentProvider,
    onFinish: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    DesignTitleBar {
        DesignScaffold(
            alwaysReturn = true,
            header = {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                    LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .windowInsetsPadding(WindowInsets.statusBars),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.padding(start = 8.dp))
                        Icon(
                            painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_left),
                            contentDescription = stringResource(id = eu.peernetwork.core.ui.R.string.back_label),
                            modifier = Modifier.size(24.dp)
                                .padding(top = 1.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(role = Role.Button) {
                                    titleBar().value?.listener?.invoke()
                                }
                        )
                        titleBar().value?.content?.invoke()
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            },
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
        ) { state ->
            DesignRouter(
                navController = controller,
                startDestination = "request",
            ) {
                composable("request") { updatedContent() }
                composable("passwordReset") {
                    PasswordResetScreen(provider, { controller.popBackStack() }, onFinish)
                }
            }
        }
    }
}
