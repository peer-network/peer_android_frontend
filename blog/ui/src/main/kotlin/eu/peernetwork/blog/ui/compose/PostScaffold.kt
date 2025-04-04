package eu.peernetwork.blog.ui.compose

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignOption
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostScaffold(
    username: State<String>,
    slug: State<String>,
    imageUrl: State<String>,
    timeStamp: State<String>,
    actions: @Composable RowScope.() -> Unit = {},
    footer: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            content()
            PostHeader(
                title = username,
                caption = slug,
                description = timeStamp,
                imageUrl = imageUrl,
                modifier = Modifier.padding(
                    vertical = 8.dp,
                    horizontal = 24.dp
                ),
                actions = actions
            )
        }
        Row(modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
        ) { footer() }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewPostScaffold() {
    PeerTheme {
        var selectedImage by remember { mutableIntStateOf(0) }
        var showFullScreen by remember { mutableStateOf(false) }
        PostScaffold(
            username = remember { mutableStateOf("Sandro") },
            slug = remember { mutableStateOf("#030604") },
            imageUrl = remember { mutableStateOf("http://localhost") },
            timeStamp = remember { mutableStateOf("2 Hours Ago") },
            footer = {
                DesignOption(
                    text = "2",
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = "Action Icon",
                    tint = Color.White,
                    onClick = {}
                )
                DesignOption(
                    text = "1",
                    painter = painterResource(id = R.drawable.ic_dislike),
                    contentDescription = "Action Icon",
                    tint = Color.White,
                    onClick = {}
                )
                Spacer(modifier = Modifier.weight(1f))
                DesignOption(
                    text = "",
                    painter = painterResource(id = R.drawable.ic_wallet_outline),
                    contentDescription = "Action Icon",
                    tint = Color.White,
                    onClick = {}
                )
            }
        ) {
            Carousel(
                count = 2,
                initialPage = selectedImage,
                colors = PhotoCarouselIndicator(
                    active = Color.Black,
                    inactive = Color.Gray
                ),
                onPhotoClick = { index ->
                    selectedImage = index
                    showFullScreen = true
                },
            ) { page ->
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .defaultMinSize(minHeight = 250.dp),
                )
            }
        }
    }
}
