package eu.peernetwork.wallet.ui.transfer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.wallet.domain.usecase.TransferUsecase

@Composable
fun TransferScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
){
    val context = LocalContext.current
    val component = remember {
        provider.builder(Transfer.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = TransferViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                TransferViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                TransferViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is TransferViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as TransferViewModel.State.Success).transfer
                    )
                }
                is TransferViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as TransferViewModel.State.Error).error
                )
            }
        }
    }

    val hardcodedRecipientId = "42935fcd-4e4e-4d89-944f-bfeb1486fc64"  // Test user ID
    val hardcodedTokenAmount = 10    // Test token amount

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                viewModel.transferToken(
                    TransferUsecase.Parameter(
                        recipient = hardcodedRecipientId,
                        numberOfTokens = hardcodedTokenAmount
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Test Hardcoded Transfer")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (val current = state) {
            is TransferViewModel.State.Empty -> {
                Text("Idle – ready to transfer")
            }
            is TransferViewModel.State.Loading -> {
                Text("Sending tokens...")
            }
            is TransferViewModel.State.Success -> {
                Text("Success: ${current.transfer.numberOfToken} to ${current.transfer.recipient}")
            }
            is TransferViewModel.State.Error -> {
                Text("Error: ${current.error.message}")
            }
        }
    }
}