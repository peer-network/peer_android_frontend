package eu.peernetwork.user.ui.deactivate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R

@Composable
fun DeactivateTitle(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_trash),
            contentDescription = stringResource(R.string.logout_text),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(size)
        )
        Text(
            text = stringResource(R.string.deactivate_text),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview
@Composable
fun PreviewConfirmationTitle() {
    DesignTheme(isDarkMode = true) {
        DeactivateTitle(modifier = Modifier.padding(16.dp))
    }
}
