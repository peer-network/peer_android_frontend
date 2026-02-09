package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.model.UiAsset
import eu.peernetwork.blog.ui.model.UiDisplay
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.blog.ui.R
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GalleryScaffold(
    type: UiPostType,
    title: AnnotatedString,
    description: AnnotatedString,
    time: String,
    asset: UiAsset,
    isVisible: MutableState<Boolean>,
    engagement: State<UiEngagement>,
    onEngage: (EngagementReaction.State) -> Unit,
    onMenu: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    toolbar: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    menu: @Composable () -> Unit = {},
    bottom: @Composable () -> Unit = {},
    content: @Composable BoxWithConstraintsScope.(UiMedia, Boolean) -> Unit
) {
    val updatedBottom by rememberUpdatedState(bottom)
    val updatedToolbar by rememberUpdatedState(toolbar)
    val updatedContent by rememberUpdatedState(content)
    val borderColor = MaterialTheme.colorScheme.surfaceDim
    val hasMedia = (type == UiPostType.VIDEO ||
            type == UiPostType.IMAGE ||
            (type == UiPostType.AUDIO && asset.media.any { it.display.cover != null }))
    Column {
        Box(
            modifier = Modifier
                .weight(1f)
                .then(modifier),
            contentAlignment = Alignment.BottomStart
        ) {
            if (hasMedia) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black))
            }
            GalleryPager(
                asset = asset,
                content = { updatedContent(it, hasMedia) }
            )
            if (hasMedia) {
                Image(
                    painter = painterResource(R.drawable.overlay_gradient),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(fraction = .9f),
                    contentScale = ContentScale.Crop
                )
            }
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = if (hasMedia) {
                    Modifier.fillMaxWidth()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = 1.dp.toPx()
                            drawLine(
                                color = borderColor,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = strokeWidth
                            )
                        }
                },
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    updatedToolbar()
                    GalleryDetail(
                        title = title,
                        time = time,
                        isVisible = isVisible,
                        description = if (type == UiPostType.TEXT) {
                            buildAnnotatedString {  }
                        } else { description },
                        onClick = onContentClick
                    )
                }
                GallerySidebar(
                    engagement = engagement.value,
                    onEngage = onEngage,
                    onMenu = onMenu,
                    modifier = Modifier.width(56.dp),
                    content = menu
                )
            }
        }
        updatedBottom()
    }
}

@Composable
@Preview
fun PreviewGalleryScaffold() {
    val asset = UiAsset(
        ratio = .5f,
        hasCover = false,
        media = persistentListOf(
            UiMedia("http://localhost", UiDisplay("", null)),
        )
    )
    val isVisible = remember { mutableStateOf(true) }
    val engagement = remember {
        derivedStateOf {
            UiEngagement(
                id = "<test-id>",
                likes = "5k",
                dislikes = "1k",
                isDisliked = false,
                isLiked = false,
                views = "3k",
                comment = "1k"
            )
        }
    }
    DesignTheme(isDarkMode = true) {
        GalleryScaffold(
            type = UiPostType.IMAGE,
            title = buildAnnotatedString { append("John Doe") },
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
            time = "2hr ago",
            asset = asset,
            isVisible = isVisible,
            engagement = engagement,
            onEngage = {},
            onMenu = {},
            onContentClick = { _,_ -> },
            toolbar = {
                GalleryToolbar(
                    slug = "239100",
                    username = "John",
                    imageUrl = "http://localhost",
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .padding(vertical = 6.dp),
                    onClick = {  },
                    content = {  }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { _, _ -> }
    }
}
