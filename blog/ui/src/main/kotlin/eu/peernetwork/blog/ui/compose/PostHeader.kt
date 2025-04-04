package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignAsyncImage
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignDetailLayout
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostHeader(
    title: State<String>,
    caption: State<String>,
    imageUrl: State<String>,
    description: State<String>,
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {}
) {
    DesignDetailLayout(
        lead = {
            DesignAvatar {
                DesignAsyncImage(
                    label = title,
                    imageUrl = imageUrl,
                    size = 48.dp
                )
            }
        },
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DesignTitle(
                title = title,
                caption = caption,
                description = description,
                verticalArrangement = Arrangement.Center,
                spacer = { },
                modifier = Modifier.padding(start = 12.dp)
                    .weight(1f)
            )
            actions()
        }
    }
}

@Preview
@Composable
fun PreviewPostHeader() {
    PeerTheme {
        PostHeader(
            title = remember { mutableStateOf("Sandro") },
            caption = remember { mutableStateOf("#030604") },
            imageUrl = remember { mutableStateOf("http://localhost") },
            description = remember { mutableStateOf("2 Hours Ago") },
            modifier = Modifier.padding(16.dp)
        ) { Text("action", modifier = Modifier.padding(16.dp)) }
    }
}
