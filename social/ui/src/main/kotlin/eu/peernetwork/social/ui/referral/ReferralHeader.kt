package eu.peernetwork.social.ui.referral

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.domain.model.Invite

@Composable
fun ReferralHeader(
    userId: String,
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
        factory = component.viewModelFactory()
    )
    val clipboardManager = LocalClipboardManager.current
    val state by viewModel.invite.collectAsState()
    val link = remember { mutableStateOf<Invite?>(
        (state as? ReferralViewModel.Status.Success)?.invite
    ) }
    val isLoading = remember { derivedStateOf {
        state is ReferralViewModel.Status.Loading
    } }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Referral Program",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Invite a friend and earn 1% of their earnings every time they transfer or cash out — forever. The more you refer, the more you earn!\n\nCopy your referral link or code and share it with the person. Make sure they enter it during registration.",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DesignOutlinedButton(
                    onClick = {
                        link.value?.let {
                            clipboardManager.setText(AnnotatedString((it.link)))
                        } ?: viewModel.invite()},
                    isLoading = isLoading.value,
                    enabled = !isLoading.value
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Copy Link")
                        Icon(
                            painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_copy),
                            contentDescription = "Copy link",
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
                DesignOutlinedButton(onClick = {
                    clipboardManager.setText(AnnotatedString(userId))
                }) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Copy Code")
                        Icon(
                            painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_copy),
                            contentDescription = "Copy code",
                            tint = MaterialTheme.colorScheme.surfaceTint,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(start = 4.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Referred people",
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
    LaunchedEffect(state) {
        when (state) {
            is ReferralViewModel.Status.Success -> {
                val invite = (state as ReferralViewModel.Status.Success).invite
                if (invite != link.value) {
                    link.value = invite
                    clipboardManager.setText(AnnotatedString((invite.link)))
                }
            }
            is ReferralViewModel.Status.Error -> {
                val error = (state as ReferralViewModel.Status.Error).error
                Toast.makeText(
                    context,
                    error.message?.let { component.resource().string(it) },
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }
}
