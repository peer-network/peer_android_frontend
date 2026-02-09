package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CheckoutSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
            .padding(top = 12.dp)
            .padding(bottom = 4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DesignSkeleton(
            modifier = Modifier.fillMaxWidth(fraction = .3f)
                .height(16.dp)
        )
        DesignSkeleton(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp)
        )
        DesignSkeleton(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .height(180.dp)
                .padding(top = 12.dp)
                .padding(bottom = 4.dp)
        )
    }
}

@Preview
@Composable
fun PreviewCheckoutSkeleton() {
    DesignTheme(isDarkMode = true) {
        CheckoutSkeleton()
    }
}
