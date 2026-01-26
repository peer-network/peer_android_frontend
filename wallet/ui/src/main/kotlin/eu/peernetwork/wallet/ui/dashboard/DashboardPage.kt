package eu.peernetwork.wallet.ui.dashboard

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.design.luna.DesignOutlineButton
import eu.peernetwork.core.ui.design.material.DesignCard
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.wallet.ui.model.UiRecipient
import java.util.UUID

@Composable
fun DashboardPage(
    name: String,
    state: State<DashboardState>,
    onClick: () -> Unit,
    content: @Composable (DashboardState.Transfer) -> Unit
) {
    val updateContent by rememberUpdatedState(content)
    Crossfade(state.value) { target ->
        when(target) {
            DashboardState.Default -> {
                DesignOutlineButton(
                    onClick = onClick,
                    minHeight = 42.dp,
                    contentPadding = PaddingValues(vertical = 8.dp, horizontal = 32.dp),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .fillMaxWidth(),
                    content = {
                        Text(text = name, style = MaterialTheme.typography.bodySmall)
                    }
                )
            }
            is DashboardState.Transfer -> { updateContent(target) }
        }
    }
}

@Composable
fun DashboardPage() {
    DesignCard(
        color = MaterialTheme.colorScheme.surfaceDim,
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        DesignAvatar {
            Box(modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewServicePage() {
    DesignTheme(isDarkMode = true) {
        Column {
            val recipient = UiRecipient(
                id = UUID.randomUUID().toString(),
                slug = "1234",
                username = "johnDoe",
                imageUrl = "http://localhost"
            )
            DashboardPage(
                "ServiceScreen",
                remember { mutableStateOf(DashboardState.Transfer(recipient)) },
                {}
            ) {
                DashboardPage()
            }
            Spacer(modifier = Modifier.height(16.dp))
            DashboardPage(
                "ServiceScreen",
                remember { mutableStateOf(DashboardState.Default) },
                {}
            ) {}
        }
    }
}
