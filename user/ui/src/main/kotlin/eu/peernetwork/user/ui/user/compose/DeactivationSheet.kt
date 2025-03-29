package eu.peernetwork.user.ui.user.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeactivationSheet(state: MutableState<Boolean>) {
    DesignBottomSheet(
        showSheet = state,
        tag = "DeactivationSheet",
        onDismissRequest = { state.value = false },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            repeat(4) { index ->
                Text(
                    "Item $index",
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (index < 19) Divider()
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { state.value = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close Sheet")
            }
        }
    }
}
