package eu.peernetwork.wallet.ui.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun OverviewScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = 16.dp,
                horizontal = 24.dp,
            ).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(.1f))
        Column(
            modifier = Modifier.weight(.9f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_icon),
                    contentDescription = stringResource(eu.peernetwork.core.ui.R.string.wallet_label),
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
                title()
            }
            content()
        }
    }
}

@Composable
fun OverviewScaffold(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        OverviewScaffold(
            title = {
                Box(modifier = Modifier.width(64.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(4.dp)))
            }
        ) {
            Box(modifier = Modifier.width(134.dp)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(4.dp)))
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.width(96.dp)
                .height(16.dp)
                .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(4.dp)))
        }
    }
}

@Preview
@Composable
fun PreviewOverviewScaffold() {
    PeerTheme {
        OverviewScaffold()
    }
}
