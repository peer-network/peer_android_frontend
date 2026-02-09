package eu.peernetwork.social.ui.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ReportScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Report.Component, ReportViewModel) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Report.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ReportViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val updatedContent by rememberUpdatedState(content)
    updatedContent(component, viewModel)
}

@Composable
fun ReportScreen(
    viewModel: ReportViewModel,
    content: @Composable (State<DesignStreamState<Unit>>) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val updatedContent by rememberUpdatedState(content)
    val derivedState = remember { derivedStateOf {
        when(state) {
            is ReportViewModel.State.Empty -> DesignStreamState.Default
            is ReportViewModel.State.Loading -> DesignStreamState.Loading
            is ReportViewModel.State.Success -> DesignStreamState.Success(Unit)
            is ReportViewModel.State.Error -> DesignStreamState.Error(
                (state as ReportViewModel.State.Error).error
            )
        }
    } }
    updatedContent(derivedState)
}
