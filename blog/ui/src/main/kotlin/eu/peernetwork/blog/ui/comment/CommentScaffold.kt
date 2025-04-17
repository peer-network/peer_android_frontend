package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CommentScaffold(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp),
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.padding(contentPadding)) { header() }
        Box(modifier = Modifier.height(1.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim.copy(alpha = .1f)))
        Box(modifier = Modifier.padding(contentPadding)) { content() }
    }
}
