package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun HomeHeader(
    state: MutableState<Int>,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    tint: Color = LocalContentColor.current,
    onClick: (HomeRoute) -> Unit = {},
    options: @Composable RowScope.() -> Unit = {},
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface,
        LocalTextStyle provides textStyle.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = modifier
                .wrapContentHeight()
                .windowInsetsPadding(WindowInsets.statusBars),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val route by remember { derivedStateOf { HomeRoute.get(state.value) } }
            Box(modifier = Modifier.padding(start = 16.dp))
            Row(
                modifier = modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable(
                        role = Role.Button,
                        onClick = { onClick(route) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(route.label), Modifier.padding(start = 8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_caret_down),
                    tint = tint,
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        alpha = if (route.hasOptions) 1f else 0f
                    }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row (
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surface)
            ) { options() }
            IconButton(onClick = { onClick(HomeRoute.Comment) }) {
                Icon(
                    painter = painterResource(id = HomeRoute.Comment.icon),
                    contentDescription = stringResource(id = HomeRoute.Comment.icon)
                )
            }
            Box(modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeHeader() {
    PeerTheme {
        val navigationState = rememberSaveable { mutableIntStateOf(0) }
        HomeHeader(state = navigationState) {
            HomeOptions()
        }
    }
}
