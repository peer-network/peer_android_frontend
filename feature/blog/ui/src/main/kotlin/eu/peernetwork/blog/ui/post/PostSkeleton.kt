package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignBox
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.design.material.DesignShimmer
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostSkeleton(count: Int) {
    LazyColumn {
        items(count) {
            PostSkeleton()
        }
    }
}

@Composable
fun PostSkeleton() {
    val color = MaterialTheme.colorScheme.surfaceContainerLow
    DesignShimmer {
        DesignBox(background = { PostScaffoldBackground() }) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(vertical = 20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    DesignSkeleton(
                        color = color,
                        modifier = Modifier.size(36.dp)
                    )
                    DesignSkeleton(
                        color = color,
                        modifier = Modifier.fillMaxWidth(fraction = .3f)
                            .padding(start = 8.dp)
                            .height(16.dp),
                    )
                }
                DesignSkeleton(
                    color = color,
                    modifier = Modifier.fillMaxWidth(fraction = .3f)
                        .padding(top = 16.dp)
                        .height(16.dp),
                )
                DesignSkeleton(
                    color = color,
                    modifier = Modifier.fillMaxWidth(fraction = .6f)
                        .padding(top = 8.dp)
                        .height(16.dp),
                )
                DesignSkeleton(
                    color = color,
                    modifier = Modifier.fillMaxWidth(fraction = .4f)
                        .padding(top = 20.dp)
                        .padding(bottom = 8.dp)
                        .height(16.dp),
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPostSkeleton() {
    DesignTheme {
        PostSkeleton(3)
    }
}
