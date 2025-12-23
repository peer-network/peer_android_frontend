package eu.peernetwork.wallet.ui.transfer

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R

@Composable
fun TransferButton(
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    onSubmit: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    IconButton(
        onClick = onSubmit,
        enabled = !isLoading && enabled,
        modifier = Modifier.then(modifier)
            .size(42.dp),
        colors = IconButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContentColor = MaterialTheme.colorScheme.outlineVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    ) {
        Icon(
            painter = painterResource(if (isLoading) {
                R.drawable.ic_more
            } else {
                eu.peernetwork.wallet.ui.R.drawable.ic_transaction
            }),
            contentDescription = null,
            tint = if (isLoading) {
                MaterialTheme.colorScheme.outlineVariant
            } else {
                MaterialTheme.colorScheme.onBackground
            },
            modifier = Modifier.size(20.dp)
                .graphicsLayer {
                    this.alpha = if (isLoading) alpha else 1f
                }
        )
    }
}
