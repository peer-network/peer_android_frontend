package eu.peernetwork.wallet.ui.transactions

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignText
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppDarkRed
import eu.peernetwork.wallet.ui.R

@Composable
fun TransactionsItem(
    title: String,
    description: AnnotatedString?,
    createAt: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    trailing: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val updatedLead by rememberUpdatedState(leading)
    val updatedTrailing by rememberUpdatedState(trailing)
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = Modifier
            .then(modifier)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceDim)
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            updatedLead()
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = createAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
                if (description != null
                    && description.isNotEmpty()) {
                    DesignText(
                        text = description,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            updatedTrailing()
        }
        updatedContent()
    }
}

@Composable
fun TransactionsItem(
    title: String,
    description: AnnotatedString?,
    price: String,
    createAt: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    leading: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val handleClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(content)
    val isVisible = rememberSaveable { mutableStateOf(false) }
    TransactionsItem(
        title = title,
        description = description,
        createAt = createAt,
        modifier = modifier,
        leading = leading,
        trailing = {
            TransactionsItemTrailing(
                price = price,
                isVisible = isVisible
            )
        },
        onClick = {
            isVisible.value = !isVisible.value
            handleClick()
        }
    ) {
        AnimatedContent(
            targetState = isVisible.value,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) +
                        expandVertically(
                            animationSpec = tween(300),
                            expandFrom = Alignment.Top
                        ) togetherWith
                        fadeOut(animationSpec = tween(200)) +
                        shrinkVertically(
                            animationSpec = tween(200),
                            shrinkTowards = Alignment.Top
                        )
            },
        ) { visible ->
            if (visible) {
                Column {
                    Box(modifier = Modifier.height(1.dp)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow))
                    updatedContent()
                }
            }
        }
    }
}

@Composable
fun TransactionsItemTrailing(
    price: String,
    isVisible: State<Boolean>
) {
    val rotation = animateFloatAsState(
        targetValue = if (isVisible.value) -90f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "TransactionsItemTrailingIndication"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = price,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold
        )
        Icon(
            painter = painterResource(R.drawable.ic_peer_token),
            contentDescription = price,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(start = 4.dp)
                .size(20.dp)
        )
        Icon(
            painter = painterResource(R.drawable.ic_caret_right),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = Modifier.size(16.dp)
                .graphicsLayer { rotationZ = rotation.value }
        )
    }
}

@Composable
@Preview
fun PreviewTransactionsItem() {
    DesignTheme(isDarkMode = true) {
        TransactionsItem(
            title = "To @removed",
            description = buildAnnotatedString { append("Hey! Thank you so much for all your help with the project presentation yesterday. I really appreciate how you stayed late to help me finalize the slides and practice the pitch. Your feedback was invaluable and I couldn't have done it without your support. The client loved it! Here's a little something to show my gratitude. Let's celebrate this weekend!") },
            price = "+534",
            createAt = "10 Jun 2025, 04:20",
            leading = {
                TransactionsAvatar(
                    icon = painterResource(R.drawable.ic_transfer_direction),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_love),
                        contentDescription = null,
                        tint = PeerAppDarkRed,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .padding(8.dp)
                    )
                }
            },
            onClick = {}
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
        }
    }
}
