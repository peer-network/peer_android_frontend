package eu.peernetwork.blog.ui.interaction.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.core.ui.component.UiComponentProvider

@Composable
fun UserList(
    id: String,
    limit: Int,
    engagement: Engagement.Content,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onUserClick: (String) -> Unit
) {
    val handleClick by rememberUpdatedState(onUserClick)
    UserScreen(
        id = id,
        limit = limit,
        engagement = engagement,
        provider = provider,
        viewModelStoreOwner
    ) { component, items ->
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                count = items.itemCount,
                key = { index -> items[index]?.id?.let { "$it;$index" } ?: index }
            ) { index ->
                items[index]?.let { user ->
                    UserItem(
                        slug = user.slug.toString(),
                        username = user.username,
                        imageUrl = user.imageUrl
                    ) { handleClick(user.id) }
                }
            }
        }
    }
}
