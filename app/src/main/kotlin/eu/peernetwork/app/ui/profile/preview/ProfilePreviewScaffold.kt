package eu.peernetwork.app.ui.profile.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return super.onPreScroll(available, source)
            }
        }
    }
    Column(modifier = modifier.fillMaxSize()
        .nestedScroll(nestedScrollConnection)) {
        header()
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        content()
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
