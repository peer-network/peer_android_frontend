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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.domain.model.Invite
import eu.peernetwork.social.ui.R

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
    val clipboardManager = LocalClipboardManager.current
    val state by viewModel.invite.collectAsState()
    val invitation = remember { mutableStateOf<Invite?>(
        (state as? ReferralViewModel.Status.Success)?.invite
    ) }
    val isLoading = remember { derivedStateOf {
        state is ReferralViewModel.Status.Loading
    } }
    val error = remember { derivedStateOf {
        (state as? ReferralViewModel.Status.Error?)?.error
    } }
    val isSuccessful = remember { derivedStateOf {
        (state as? ReferralViewModel.Status.Success?)?.invite
    } }
    ReferralHeader(
        isLoading = isLoading,
        onClick = {
            invitation.value?.let {
                clipboardManager.setText(AnnotatedString((it.link)))
            } ?: viewModel.invite()
        }
    )
    LaunchedEffect(isSuccessful.value) {
        isSuccessful.value?.let {
            if (it != invitation.value) {
                invitation.value = it
                clipboardManager.setText(AnnotatedString((it.link)))
            }
        }
    }
    LaunchedEffect(error.value) {
        error.value?.message?.let {
            Toast.makeText(
                context,
                component.resource().string(it),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

@Composable
fun ReferralHeader(
    isLoading: State<Boolean>,
    onClick: () -> Unit
) {
    val border = MaterialTheme.colorScheme.tertiaryContainer
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = border,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }.padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Text(
            text = stringResource(R.string.referrals_header),
            style = MaterialTheme.typography.headlineLarge.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.referrals_description),
            style = MaterialTheme.typography.labelLarge.copy(
                color = MaterialTheme.colorScheme.tertiary
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        DesignOutlinedButton(
            onClick = onClick,
            isLoading = isLoading.value,
            enabled = !isLoading.value,
            minHeight = 42.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 2.dp)
            ) {
                Text(
                    stringResource(R.string.referrals_link),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_copy),
                    contentDescription = stringResource(R.string.referrals_link),
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview
@Composable
fun PreviewReferralHeader() {
    PeerTheme {
        ReferralHeader(remember { mutableStateOf(false) }) {}
    }
}
