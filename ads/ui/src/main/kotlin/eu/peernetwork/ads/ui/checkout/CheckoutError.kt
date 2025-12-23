package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CheckoutError(
    error: State<Throwable?>,
    component: Checkout.Component,
    onRefresh: () -> Unit
) {
    DesignScaffold {
        error.value?.message?.let {
            CheckoutError(
                error = component.resource().string(it),
                onRefresh = onRefresh
            )
        }
    }
}

@Composable
fun CheckoutError(
    error: String,
    onRefresh: () -> Unit
) {
    DesignSkeleton(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(24.dp))
            .heightIn(min = 180.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.Center)
                .padding(24.dp)
        ) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            DesignButton(
                onClick = onRefresh,
                minHeight = 42.dp,
                contentPadding = PaddingValues(
                    vertical = 12.dp,
                    horizontal = 36.dp
                ),
                colors = designSecondaryButtonColors(),
                modifier = Modifier.padding(top = 10.dp),
            ) { Text(stringResource(R.string.retry_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewCheckoutError() {
    DesignTheme(isDarkMode = true) {
        CheckoutError(
            error = "An unexpected error occurred while processing your payment. Please try again.",
            onRefresh = {}
        )
    }
}
