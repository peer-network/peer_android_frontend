package eu.peernetwork.app.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.post.PostSkeleton
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun HomeSkeleton() {
    DesignPage(
        header = {
            DesignSkeleton(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 10.dp)
                    .height(56.dp)
            )
        },
        footer = {
            DesignSkeleton(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(vertical = 10.dp)
                    .height(56.dp)
            )
        }
    ) { PostSkeleton(3) }
}


@Preview
@Composable
fun PreviewHomeSkeleton() {
    DesignTheme(isDarkMode = true) {
        HomeSkeleton()
    }
}
