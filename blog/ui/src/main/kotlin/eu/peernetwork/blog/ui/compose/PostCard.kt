package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostCard(
    header: @Composable () -> Unit,
    toolbar: @Composable () -> Unit,
    background: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(
        vertical = 16.dp,
        horizontal = 24.dp,
    ),
    content: @Composable () -> Unit,
) {
    val updatedHeader by rememberUpdatedState(header)
    val updatedToolbar by rememberUpdatedState(toolbar)
    val updatedContent by rememberUpdatedState(content)
    val updatedFooter by rememberUpdatedState(footer)
    val updatedBackground by rememberUpdatedState(background)
    Column(modifier = modifier) {
        Layout(
            content = {
                Box { updatedBackground() }
                Column(modifier = Modifier.padding(contentPadding)) {
                    updatedHeader()
                    updatedContent()
                    updatedFooter()
                }
            }
        ) { measurables, constraints ->
            val backgroundMeasurable = measurables[0]
            val columnMeasurable = measurables[1]
            val columnPlaceable = columnMeasurable.measure(constraints)
            val width = columnPlaceable.width
            val height = columnPlaceable.height
            val backgroundPlaceable = backgroundMeasurable.measure(
                Constraints.fixed(width, height)
            )
            layout(width, height) {
                backgroundPlaceable.place(0, 0)
                columnPlaceable.place(0, 0)
            }
        }
        updatedToolbar()
    }
}

@Preview
@Composable
fun PreviewPostCard() {
    PeerTheme {
        PostCard(
            header = { Box(modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.secondary)
            ) },
            background = {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                )
            },
            footer = { Box(modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.error)
            ) },
            toolbar = { Box(modifier = Modifier.fillMaxWidth()
                .height(48.dp)
                .background(MaterialTheme.colorScheme.secondary)
            ) }
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(56.dp)
                .border(1.dp, MaterialTheme.colorScheme.primary)
            )
        }
    }
}
