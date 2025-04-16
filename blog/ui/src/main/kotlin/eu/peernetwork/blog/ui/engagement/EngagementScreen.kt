package eu.peernetwork.blog.ui.engagement

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun EngagementScreen(
    post: UiPost,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onComment: () -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Engagement.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = EngagementViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    Row {
        PostIcon(UiAction.Like, post.likes.toString(), {  })
        PostIcon(UiAction.Dislike, post.dislikes.toString(), {  })
        PostIcon(UiAction.Comment, post.comment.toString(), {  })
    }
//    val coroutineScope = rememberCoroutineScope()
//
//    Row {
//        PostIcon(
//            UiAction.Like,
//            post.likes.toString(),
//            onClick = {
//                coroutineScope.launch {
//                    viewModel.create(post.id, Engagement.Content.Like)
//                }
//            }
//        )
//        PostIcon(
//            UiAction.Like,
//            post.dislikes.toString(),
//            onClick = {
//                coroutineScope.launch {
//                    viewModel.create(post.id, Engagement.Content.Dislike)
//                }
//            }
//        )
//        PostIcon(UiAction.Comment, post.comment.toString(), onClick)
//    }
}

