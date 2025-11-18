package eu.peernetwork.ads.ui.checkout

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton

@Composable
fun CheckoutSummery() {
    DesignSkeleton(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier.fillMaxWidth()
        .height(120.dp)
    )
}
