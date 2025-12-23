package eu.peernetwork.wallet.ui.overview

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.R

@Composable
fun OverviewScaffold(
    modifier: Modifier = Modifier,
    rate: @Composable () -> Unit,
    token: @Composable () -> Unit,
    balance: @Composable () -> Unit,
) {
    val updatedRate by rememberUpdatedState(rate)
    val updatedToken by rememberUpdatedState(token)
    val updatedBalance by rememberUpdatedState(balance)
    val top = with(LocalDensity.current) { 280.dp.toPx() }
    val verticalGradient = Brush.linearGradient(
        start = Offset(top, 0f),
        colors = listOf(
            MaterialTheme.colorScheme.surfaceDim,
            MaterialTheme.colorScheme.primary
        ),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .background(brush = verticalGradient)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(stringResource(R.string.wallet_title))
                Spacer(modifier = Modifier.weight(1f))
                updatedRate()
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_icon),
                contentDescription = stringResource(eu.peernetwork.core.ui.R.string.wallet_label),
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            updatedToken()
        }
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodySmall
        ) {
            updatedBalance()
        }
        Spacer(modifier = Modifier.height(36.dp))
    }
}

@Composable
fun OverviewScaffold(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        OverviewScaffold(
            rate = {
                Box(modifier = Modifier.width(32.dp)
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp)))
            },
            token = {
                Box(modifier = Modifier.width(120.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp)))
            }
        ) {
            Box(modifier = Modifier.width(72.dp)
                .height(12.dp)
                .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(4.dp)))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewOverviewScaffold() {
    DesignTheme(isDarkMode = true) {
        OverviewScaffold()
    }
}
