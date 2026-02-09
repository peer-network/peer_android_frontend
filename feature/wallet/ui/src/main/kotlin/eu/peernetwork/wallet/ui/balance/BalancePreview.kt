package eu.peernetwork.wallet.ui.balance

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.wallet.ui.R

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun BalancePreview(
    balance: String,
    modifier: Modifier = Modifier,
    color: Color? = null,
    contentPadding: PaddingValues = PaddingValues(24.dp),
) {
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
                .background(color ?: MaterialTheme.colorScheme.surfaceDim)
                .then(if (color != null) {
                    Modifier
                } else {
                    Modifier.background(brush = verticalGradient)
                })
                .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = balance,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_icon),
                contentDescription = stringResource(eu.peernetwork.core.ui.R.string.wallet_label),
                modifier = Modifier.padding(start = 4.dp)
                    .size(42.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
fun PreviewServicePage() {
    DesignTheme(isDarkMode = true) {
        BalancePreview(
            balance = "2 234"
        )
    }
}
