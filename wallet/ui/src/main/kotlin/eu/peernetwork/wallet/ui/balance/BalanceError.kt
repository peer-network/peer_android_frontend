package eu.peernetwork.wallet.ui.balance

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun BalanceError(
    error: State<Throwable?>,
    component: Balance.Component,
    onRefresh: () -> Unit
) {
    error.value?.message?.let {
        BalanceError(
            error = component.resource().string(it),
            onRefresh = onRefresh
        )
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun BalanceError(
    error: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(24.dp),
    onRefresh: () -> Unit
) {
    val handleRefresh by rememberUpdatedState(onRefresh)
    BoxWithConstraints {
        val vertical = with(LocalDensity.current) { maxHeight.toPx() * .15f }
        val horizontal = with(LocalDensity.current) { maxHeight.toPx() * .1f }
        val verticalGradient = Brush.linearGradient(
            start = Offset(0f, vertical),
            colors = listOf(
                MaterialTheme.colorScheme.surfaceDim,
                MaterialTheme.colorScheme.surfaceDim,
                MaterialTheme.colorScheme.primary.copy(alpha = .5f)
            ),
            end = Offset(horizontal, Float.POSITIVE_INFINITY)
        )
        Row(
            modifier = Modifier.then(modifier)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceDim)
                .background(brush = verticalGradient)
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onRefresh) {
                Icon(
                    painter = painterResource(R.drawable.ic_refresh),
                    contentDescription = stringResource(R.string.retry_label),
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp)
                        .size(36.dp)
                        .clickable { handleRefresh() },
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewBalanceError() {
    DesignTheme(isDarkMode = true) {
        BalanceError(
            error = "An unexpected error occurred while processing your payment. Please try again."
        ) {}
    }
}
