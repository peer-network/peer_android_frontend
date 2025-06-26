package eu.peernetwork.social.ui.referral

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
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
        factory = component.viewModelFactory()
    )
    LaunchedEffect(Unit) {
        viewModel.invite()
    }
    val clipboardManager = LocalClipboardManager.current
    val referralMessage = "Referral code copied"
    val inviteState by viewModel.invite.collectAsState()

    when (val state = inviteState) {
        ReferralViewModel.Status.Empty -> DesignStatefulScaffoldState.Empty
        ReferralViewModel.Status.Loading -> DesignStatefulScaffoldState.Loading
        is ReferralViewModel.Status.Success -> {
            val link = state.invite.link
            val code = link.removePrefix("https://frontend.getpeer.eu/register.php?referralUuid=")

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Referral Program",
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Invite a friend and earn 1% of their earnings every time they transfer or cash out — forever. The more you refer, the more you earn!\n\nCopy your referral link or code and share it with the person. Make sure they enter it during registration.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        DesignOutlinedButton(onClick = {
                            clipboardManager.setText(AnnotatedString(link))
                        }) {
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
                            clipboardManager.setText(AnnotatedString(code))
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
                    style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onBackground)
                )
            }
        }
        is ReferralViewModel.Status.Error -> {
            Text(
                text = "Failed to load invite link.",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}