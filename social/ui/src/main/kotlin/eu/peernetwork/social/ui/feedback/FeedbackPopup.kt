package eu.peernetwork.social.ui.feedback

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.extension.builder
import kotlinx.coroutines.delay

@Composable
fun FeedbackPopup(
    appPackage: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feedback.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = FeedbackViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )

    val state by viewModel.state.collectAsStateWithLifecycle()
    val showSheet = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    if (state is FeedbackViewModel.State.Success) {
        showSheet.value = true
    }

    DesignBottomSheetScaffold(
        state = showSheet,
        onDismiss = { showSheet.value = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Enjoying the app? Give us feedback!")
            Spacer(modifier = Modifier.height(16.dp))
            if (state is FeedbackViewModel.State.Success) {
                Text("Popup shown ${(state as FeedbackViewModel.State.Success).counter} times")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(onClick = { showSheet.value = false }) {
                    Text("Close")
                }
                Button(onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                    )
                    context.startActivity(intent)
                    showSheet.value = false
                    viewModel.observe()
                }) {
                    Text("Give Feedback")
                }
            }
        }
    }
}
