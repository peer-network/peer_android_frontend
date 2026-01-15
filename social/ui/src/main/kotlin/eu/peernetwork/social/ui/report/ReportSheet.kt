package eu.peernetwork.social.ui.report

import android.widget.Toast
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold

@Composable
fun ReportSheet(
    state: MutableState<String?>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val streamState = remember { derivedStateOf {
        if (state.value == null) {
            DesignStreamState.Default
        } else {
            DesignStreamState.Success(state.value!!)
        }
    } }
    val errorMessage = stringResource(R.string.unknown_error_message)
    ReportScreen(
        provider = provider,
        viewModelStoreOwner = viewModelStoreOwner
    ) { component, viewModel ->
        val reportState by viewModel.state.collectAsStateWithLifecycle()
        val code = remember { derivedStateOf {
            (reportState as? ReportViewModel.State.Success?)?.code
        } }
        val isLoading = remember { derivedStateOf {
            reportState is ReportViewModel.State.Loading
        } }
        val error = remember { derivedStateOf {
            (reportState as? ReportViewModel.State.Error?)?.error
        } }
        DesignStream(streamState) { post ->
            DesignBottomSheetScaffold(
                state = showSheet,
                dismissable = true,
                onDismiss = {
                    state.value = null
                    viewModel.reset()
                }
            ) {
                ReportScreen(viewModel) { stream ->
                    ReportPage(
                        isLoading = isLoading,
                        onCancel = { state.value = null },
                        modifier = Modifier.padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                            .padding(bottom = 8.dp)
                            .navigationBarsPadding()
                    ) { viewModel(post.value) }
                }
            }
        }
        LaunchedEffect(code.value) {
            if (code.value != null) {
                Toast.makeText(
                    context,
                    component.resource().string(code.value!!),
                    Toast.LENGTH_LONG
                ).show()
                state.value = null
                viewModel.reset()
            }
        }
        LaunchedEffect(error.value) {
            if (error.value != null) {
                val message = error.value?.message ?: errorMessage
                Toast.makeText(
                    context,
                    component.resource().string(message),
                    Toast.LENGTH_LONG
                ).show()
                state.value = null
                viewModel.reset()
            }
        }
    }
}
