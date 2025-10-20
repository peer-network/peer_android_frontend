package eu.peernetwork.user.ui.v2.registration

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.user.ui.R

@Composable
fun RegistrationScreen(
    referral: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: androidx.lifecycle.ViewModelStoreOwner,
    onPrivacy: () -> Unit,
    onLicence: () -> Unit,
    onLogin: () -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Registration.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = RegistrationViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is RegistrationViewModel.State.Loading } }
    val isRegistered = remember { derivedStateOf {
        (state as? RegistrationViewModel.State.Success?)?.uuid != null
    } }
    val error = remember { derivedStateOf {
        (state as? RegistrationViewModel.State.Error?)?.error?.message?.let {
            component.resource().string(it)
        }
    } }
    val handleOnLogin by rememberUpdatedState(onLogin)
    val message = stringResource(R.string.successful_message)
    RegistrationPage(
        isLoading = isLoading,
        error = error,
        onLogin = onLogin,
        onPrivacy = onPrivacy,
        onLicence = onLicence
    ) {
        viewModel.register(
            username = it.username,
            email = it.email,
            password = it.password,
            referral = referral
        )
    }
    LaunchedEffect(isRegistered.value) {
        if (isRegistered.value) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            handleOnLogin()
        }
    }
}
