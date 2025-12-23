package eu.peernetwork.ads.ui.checkout

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.model.UiCharge
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.theme.DesignTheme
import java.math.BigDecimal

@Composable
fun CheckoutPage(
    charge: UiCharge,
    price: BigDecimal,
    isLoading: State<Boolean>,
    error: State<String?>,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onPay: () -> Unit,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .padding(bottom = 4.dp)
            .then(modifier)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.boost_confirmation_label),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        updatedContent()
        CheckoutSummery(
            price = price,
            charge = charge,
            modifier = Modifier.padding(vertical = 12.dp)
        )
        AnimatedContent(targetState = error.value) { message ->
            if (message != null) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 8.dp)
                        .padding(bottom = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier.padding(top = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DesignOutlineButton(
                onClick = onBack,
                minHeight = 48.dp,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline
                )
            ) { Text(stringResource(R.string.back_label)) }
            DesignButton(
                onClick = onPay,
                minHeight = 48.dp,
                enabled = !isLoading.value,
                isLoading = isLoading.value,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f),
            ) { Text(stringResource(R.string.pay_label)) }
        }
    }
}

@Preview
@Composable
fun PreviewCheckoutPage() {
    DesignTheme(isDarkMode = true) {
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf<String?>(null) }
        CheckoutPage(
            price = BigDecimal.TEN,
            charge = UiCharge(
                percentage = 0.1,
                peer = 0.05,
                burn = 0.02,
                pool = 0.1
            ),
            isLoading = isLoading,
            error = error,
            modifier = Modifier.padding(vertical = 16.dp),
            onBack = {  },
            onPay = {}
        ) {}
    }
}
