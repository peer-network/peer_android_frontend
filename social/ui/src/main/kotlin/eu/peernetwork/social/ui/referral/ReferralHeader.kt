package eu.peernetwork.social.ui.referral

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ReferralHeader(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Referral.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ReferralViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
    )

    val clipboardManager = LocalClipboardManager.current

    val inviteState by viewModel.invite.collectAsState()

    // Trigger invite loading when composable enters composition
    LaunchedEffect(Unit) {
        viewModel.invite()
    }

    when (val state = inviteState) {
        is ReferralViewModel.Status.Success -> {
            val link = state.invite.link
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(text = "Your Invite Link:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = link,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        clipboardManager.setText(AnnotatedString(link))
                    }
                )
            }
        }

        is ReferralViewModel.Status.Loading -> {
            DesignStatefulScaffoldState.Loading
        }

        is ReferralViewModel.Status.Error -> {
            Text(
                text = "Failed to load invite link.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }

        ReferralViewModel.Status.Empty -> {
        }
    }
}