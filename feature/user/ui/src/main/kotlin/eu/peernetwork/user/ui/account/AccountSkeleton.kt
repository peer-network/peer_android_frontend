package eu.peernetwork.user.ui.account

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignSkeleton
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun AccountSkeleton() {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DesignAvatar {
            DesignSkeleton(modifier = Modifier.size(88.dp))
        }
        Spacer(modifier = Modifier.height(28.dp))
        DesignSkeleton(modifier = Modifier.height(48.dp)
            .fillMaxWidth())
        DesignSkeleton(
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(top = 12.dp)
                .height(96.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAccountSkeleton() {
    DesignTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            AccountSkeleton()
        }
    }
}
