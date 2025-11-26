package eu.peernetwork.blog.ui.post

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PostModal() {
    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.background))
}
