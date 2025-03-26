package eu.peernetwork.app.ui.profile.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun ProfilePreviewScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        Column {
            header()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ProfileMedia.ROUTES.forEach {
                    Icon(
                        painter = painterResource(id = it.id),
                        contentDescription = it.label?.let { stringResource(it) },
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.padding(vertical = 8.dp).size(28.dp).weight(1f)
                    )
                }
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                thickness = 1.dp
            )
        }
        Box(modifier = Modifier.fillMaxWidth()
            .weight(1f)) { content() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewProfilePreviewScaffold() {
    PeerTheme {
        ProfilePreviewScaffold(
            modifier = Modifier.fillMaxSize(),
            header = { Text("Header") },
            content = { Text("Content") },
        )
    }
}
