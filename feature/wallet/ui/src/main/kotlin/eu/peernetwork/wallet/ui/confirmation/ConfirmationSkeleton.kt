package eu.peernetwork.wallet.ui.confirmation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun ConfirmationSkeleton() {
    Column(modifier = Modifier.padding(16.dp)
        .navigationBarsPadding()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.height(24.dp)
                    .aspectRatio(1f)
            )
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.height(16.dp)
                    .weight(.3f)
            )
            Spacer(modifier = Modifier.weight(.7f))
        }
        DesignSkeleton(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.padding(top = 20.dp)
                .fillMaxWidth(fraction = .9f)
                .height(16.dp)
        )
        Row(modifier = Modifier.padding(top = 20.dp)
            .padding(bottom = 6.dp)) {
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.weight(1f)
                    .height(42.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            DesignSkeleton(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.weight(1f)
                    .height(42.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewConfirmationSkeleton() {
    DesignTheme(isDarkMode = true) {
        Box(modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceDim)) {
            ConfirmationSkeleton()
        }
    }
}
