package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.design.material.DesignPageHeader
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun HomePage(
    start: State<Int>,
    options: @Composable () -> Unit,
    onClick: (Int) -> Unit,
    onExplore: () -> Unit,
    isExploreActive: Boolean,
    content: @Composable (State<Float>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnClick by rememberUpdatedState(onClick)
    val handleOnExplore by rememberUpdatedState(onExplore)
    DesignPage(
        header = {
            DesignPageHeader(
                options = options,
                action = {
                    IconButton(onClick = {
                        handleOnExplore()
                    }) {
                        Icon(
                            painter = painterResource(id = if (isExploreActive) {
                                HomeMenu.Explore.active
                            } else {
                                HomeMenu.Explore.icon
                            }),
                            contentDescription = stringResource(id = HomeMenu.Explore.icon),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) },
        footer = {
            HomeFooter(
                start,
                onClick = { prev, next ->
                    if (prev == next) {
                        titleBar().value?.listener?.invoke()
                    } else {
                        handleOnClick(next)
                    }
                }
            ) }
    ) { state -> updatedContent(state) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomePage() {
    DesignTheme(isDarkMode = true) {
        HomePage(
            start = remember { mutableIntStateOf(0) },
            options = {},
            onClick = {},
            onExplore = {},
            isExploreActive = false
        ) { state ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
