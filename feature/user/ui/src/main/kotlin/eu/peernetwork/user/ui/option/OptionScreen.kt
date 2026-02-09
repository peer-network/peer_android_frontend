package eu.peernetwork.user.ui.option

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.feature.user.ui.R

@Composable
fun OptionScreen(
    isAdmin: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBlock: () -> Unit,
    onMenuClicked: () -> Unit,
    onSettings: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Option.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = OptionViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isInvited = remember { derivedStateOf { state as? OptionViewModel.State.Success? } }
    val isLoading = remember { derivedStateOf { state is OptionViewModel.State.Loading } }
    val inviteLink = remember { mutableStateOf<String?>(null) }
    val inviteLabel = stringResource(R.string.invite_label)
    val showOption = remember { mutableStateOf(false) }
    if (isAdmin) {
        OptionMenu(
            isLoading = isLoading,
            onBoost = { showOption.value = !showOption.value },
            onInvite = {
                inviteLink.value?.let {
                    context.sendInvitation(inviteLabel, it)
                } ?: viewModel.invite()
            },
            onSettings = onSettings
        )
    } else {
        OptionMenu(
            onMenu = { showOption.value = true },
            content = content
        )
    }
    OptionSheet(
        isAdmin = isAdmin,
        state = showOption,
        onBlock = onBlock,
        onMenuClicked = onMenuClicked
    )
    LaunchedEffect(isInvited.value) {
        if (isInvited.value != null) {
            isInvited.value?.invite?.link?.let {
                inviteLink.value = it
            }
        }
    }
    LaunchedEffect(inviteLink.value) {
        inviteLink.value?.let {
            context.sendInvitation(inviteLabel, it)
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.reset() }
    }
}

private fun Context.sendInvitation(label: String, link: String) {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, link)
    }
    val shareIntent = Intent.createChooser(sendIntent, label)
    startActivity(shareIntent)
}
