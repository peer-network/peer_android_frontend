package eu.peernetwork.wallet.ui.confirmation

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiIntent
import eu.peernetwork.wallet.ui.model.UiQuote
import eu.peernetwork.wallet.ui.model.UiWallet

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ConfirmationScreen(
    intent: UiIntent,
    showSheet: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onDismiss: () -> Unit,
    onConfirm: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Confirmation.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ConfirmationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val handleOnConfirm by rememberUpdatedState(onConfirm)
    val derivedState = remember { derivedStateOf {
        when(state) {
            ConfirmationViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            ConfirmationViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is ConfirmationViewModel.State.Success -> {
                val data = (state as ConfirmationViewModel.State.Success)
                DesignStatefulScaffoldState.Success(Pair(data.quote, data.wallet))
            }
            is ConfirmationViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as ConfirmationViewModel.State.Error).error)
            }
        }
    } }
    DesignBottomSheet(
        showSheet = showSheet,
        tag = "ConfirmationScreen",
        onDismissRequest = onDismiss,
        background = {
            DesignOverlayBackground(
                state = it,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = .6f))
            )
        }
    ) {
        DesignStatefulScaffold<Pair<UiQuote, UiWallet>>(
            derivedState,
            onRefresh = { viewModel.initialize(intent) },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding(),
            placeholder = { Box(Modifier.height(300.dp)) }
        ) {
            ConfirmationScreen({ showSheet.value = false }) {
                handleOnConfirm(true)
            }
        }
    }
}

@Composable
fun ConfirmationScreen(
    onCancel: () -> Unit,
    onSend: () -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.surfaceVariant
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    ConfirmationScaffold(
        title = {
            Text(stringResource(R.string.post_caption))
        },
        footer = {
            Row {
                DesignOutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(28),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        color = onPrimary,
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = onPrimary,
                        disabledContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, onPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) { Text(stringResource(R.string.cancel_label)) }
                Spacer(modifier = Modifier.width(12.dp))
                DesignOutlinedButton(
                    onClick = onSend,
                    modifier = Modifier.background(
                        color = onPrimary,
                        shape = RoundedCornerShape(28),
                    ).weight(1f),
                    shape = RoundedCornerShape(28),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor,
                        fontWeight = FontWeight.SemiBold
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = onPrimary,
                        disabledContainerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, onPrimary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) { Text(stringResource(R.string.confirm_label)) }
            }
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_chat),
                contentDescription = stringResource(eu.peernetwork.core.ui.R.string.chat_label),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                stringResource(R.string.post_condition, "$5", "$10"),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(.3f))
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewConfirmationScreen() {
    PeerTheme {
        ConfirmationScreen({}) {}
    }
}
