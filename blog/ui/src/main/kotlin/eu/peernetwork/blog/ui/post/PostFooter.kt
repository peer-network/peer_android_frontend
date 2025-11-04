package eu.peernetwork.blog.ui.post

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PostFooter() {
    Row {
        
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewPostFooter() {
    DesignTheme(isDarkMode = false) {
        PostFooter()
    }
}
