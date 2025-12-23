package eu.peernetwork.media.ui.power

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun PowerError(
    translucent: Boolean = false,
    onClick: () -> Unit
) {
    PowerButton(
        translucent = translucent,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_warning),
            contentDescription = stringResource(eu.peernetwork.core.ui.R.string.error_label),
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
fun PreviewPowerError() {
    DesignTheme(isDarkMode = true) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { PowerError(false) {} }
    }
}
