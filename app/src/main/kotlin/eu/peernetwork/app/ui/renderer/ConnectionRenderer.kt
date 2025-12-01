package eu.peernetwork.app.ui.renderer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.blog.ui.post.PostUserConnection
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionInteractor.Companion.LocalConnectionInteractor

class ConnectionRenderer : PostUserConnection {
    @Composable
    override fun invoke(
        modifier: Modifier,
        spec: PostUserConnection.Spec
    ) {
        val controller = LocalConnectionInteractor.current
        val connection = controller.observe().collectAsStateWithLifecycle()
        ConnectionButton(
            isFollowing = connection.value.getOrDefault(spec.id, spec.isFollowing),
            isFollowed = spec.isFollowed,
            onClick = { follow ->
                controller.invoke(spec.id, !follow)
            },
            fontWeight = FontWeight.SemiBold,
            minHeight = 32.dp,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )
    }
}
