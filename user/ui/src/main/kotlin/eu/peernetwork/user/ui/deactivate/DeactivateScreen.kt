package eu.peernetwork.user.ui.deactivate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.design.material.DesignButton
import eu.peernetwork.core.ui.design.material.DesignSecureTextField
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.user.ui.R

@Composable
fun DeactivateScreen(
    show: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Deactivate.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = DeactivateViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val isLoading = remember { derivedStateOf { state is DeactivateViewModel.State.Loading } }
    val isSuccess = remember { derivedStateOf { state is DeactivateViewModel.State.Success } }
    val password = remember { TextFieldState() }
    val focus = remember { FocusRequester() }
    DesignBottomSheetScaffold(
        state = show,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        onShow = { focus.requestFocus() },
        onDismiss = { show.value = false }
    ) {
        DeactivateScreen(
            state = show,
            isLoading = isLoading,
            password = password,
            label =  stringResource(R.string.deactivate_text),
            focus = focus
        ) { viewModel(password.text.toString()) }
    }
    LaunchedEffect(isSuccess.value) {
        if (isSuccess.value) {
            show.value = false
            viewModel.reset()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
fun DeactivateScreen(
    state: MutableState<Boolean>,
    isLoading: State<Boolean>,
    password: TextFieldState,
    label: String,
    focus: FocusRequester,
    onSubmit: (String) -> Unit = {}
) {
    val isValidated = remember { derivedStateOf {
        state.value && password.isValidInput() && !isLoading.value
    } }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        DesignSecureTextField(
            state = password,
            focusRequester = focus,
            enabled = state.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            placeholder = { Text(stringResource(id = R.string.password_label)) },
            textObfuscationMode = TextObfuscationMode.Hidden,
        )
        Spacer(modifier = Modifier.height(16.dp))
        DesignButton(
            enabled = isValidated.value,
            isLoading = isLoading.value,
            onClick = { onSubmit(password.text.toString()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
        Spacer(modifier = Modifier.navigationBarsPadding())
    }
}
