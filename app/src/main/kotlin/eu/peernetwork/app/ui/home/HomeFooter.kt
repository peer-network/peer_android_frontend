package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeRoute.ROUTES.forEachIndexed { index, navigation ->
            IconButton(onClick = { state.value = index }) {
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

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeBottomBar() {
    PeerTheme {
        HomeFooter(state = rememberSaveable { mutableIntStateOf(0) })
    }
}
