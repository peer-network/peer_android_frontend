package eu.peernetwork.blog.ui.gallery

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.engagement.EngagementReaction
import eu.peernetwork.blog.ui.model.UiAsset
import eu.peernetwork.blog.ui.model.UiDisplay
import eu.peernetwork.blog.ui.model.UiEngagement
import eu.peernetwork.blog.ui.model.UiMedia
import eu.peernetwork.blog.ui.model.UiPostType
import eu.peernetwork.core.ui.design.luna.DesignRichText
import eu.peernetwork.core.ui.theme.DesignTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun GalleryScaffold(
    slug: String,
    username: String,
    type: UiPostType,
    title: AnnotatedString,
    description: AnnotatedString,
    imageUrl: String,
    time: String,
    asset: UiAsset,
    engagement: State<UiEngagement>,
    onEngage: (EngagementReaction.State) -> Unit,
    onMenu: () -> Unit,
    showAuthor: () -> Unit,
    onContentClick: (DesignRichText, String) -> Unit,
    modifier: Modifier = Modifier,
    connection: @Composable () -> Unit = {},
    content: @Composable BoxWithConstraintsScope.(String) -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.surfaceDim
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        GalleryPager(
            asset = asset,
            content = content
        )
        if (type != UiPostType.TEXT) {
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
            modifier = if (type != UiPostType.TEXT) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.fillMaxWidth()
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
                GalleryToolbar(
                    slug = slug,
                    username = username,
                    imageUrl = imageUrl,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .padding(vertical = 6.dp),
                    onClick = showAuthor,
                    content = connection
                )
                GalleryDetail(
                    title = title,
                    time = time,
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
                Modifier.width(56.dp)
            )
        }
    }
}

@Composable
@Preview
fun PreviewGalleryScaffold() {
    val asset = UiAsset(
        ratio = .5f,
        media = persistentListOf(
            UiMedia("http://localhost", UiDisplay("", null)),
        )
    )
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
            slug = "239100",
            username = "John",
            type = UiPostType.IMAGE,
            title = buildAnnotatedString { append("John Doe") },
            description = buildAnnotatedString {
                append("This is a mock description for a content post. It's purely for testing.")
            },
            imageUrl = "http://localhost",
            time = "2hr ago",
            asset = asset,
            engagement = engagement,
            onEngage = {},
            onMenu = {},
            showAuthor = {},
            onContentClick = { _,_ -> },
            modifier = Modifier.fillMaxSize()
        ) {}
    }
}
