package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun HomeFooter(
    state: MutableState<Int>,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit = {},
) {
    val handleClick by rememberUpdatedState(onClick)
    Box(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HomeRoute.ROUTES.forEachIndexed { index, navigation ->
                IconButton(onClick = {
                    if (state.value == index) {
                        handleClick(index)
                    }
                    state.value = index
                }) {
                    Box {
                        Icon(
                            tint = MaterialTheme.colorScheme.onSurface,
                            painter = painterResource(id = navigation.icon),
                            contentDescription = stringResource(id = navigation.label),
                            modifier = Modifier.size(32.dp).graphicsLayer {
                                alpha = if (index == state.value) 0f else 1f
                            }
                        )
                        Icon(
                            tint = MaterialTheme.colorScheme.onBackground,
                            painter = painterResource(id = navigation.activeIcon),
                            contentDescription = stringResource(id = navigation.label),
                            modifier = Modifier.size(32.dp).graphicsLayer {
                                alpha = if (index == state.value) 1f else 0f
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeBottomBar() {
    PeerTheme {
        HomeFooter(state = remember { mutableIntStateOf(0) })
    }
}
