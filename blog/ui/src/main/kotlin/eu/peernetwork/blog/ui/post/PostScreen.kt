package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.blog.ui.model.UiPost.Type
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun PostScreen(
    id: String,
    limit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    connection: @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit,
    content: @Composable (PostEvent) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Post.Builder::class.java).build(context)
    }
    val updatedContent by rememberUpdatedState(content)
    EngagementScreen(
        userId = id,
        postLimit = limit,
        onAuthorClick = {  },
        onMentionClick = {  },
        onHashtagClick = {  },
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner,
        connection = connection
    ) { engagement ->
        val event = remember { object : PostEvent {} }
        ModerationScreen(
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { moderation ->
            updatedContent(event)
        }
    }
}

@Composable
fun PostScreen(
    type: Type,
    pinnedBy: String?,
    model: UiPost.Detail,
    engagement: UiPost.Engagement,
    component: Post.Component,
    connection: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    if (type == Type.TEXT) {
        PostScaffold(
            model = model,
            pinnedBy = pinnedBy,
            engagement = engagement,
            connection = connection,
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(260.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        }
    } else if (type == Type.IMAGE) {
        PostScaffold(
            model = model,
            engagement = engagement,
            pinnedBy = pinnedBy,
            connection = connection
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(260.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerLowest))
        }
    }
}
