package eu.peernetwork.app.ui.creator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.compose.DesignButton
import eu.peernetwork.core.ui.compose.DesignTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextCreate(
    onPostClick: () -> Unit = {}
) {
    val titleState = remember { TextFieldState() }
    val descriptionState = remember { TextFieldState() }
    var hasError by remember { mutableStateOf(false) }
    val bothFieldsFilled = titleState.text.isNotEmpty() && descriptionState.text.isNotEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DesignTextField(
                state = titleState,
                modifier = Modifier.fillMaxWidth(),
                hasError = hasError && titleState.text.isEmpty(),
                error = {
                    if (titleState.text.isEmpty()) {
                        Text("Title cannot be empty")
                    }
                }
            ) {
                Text("Title")
            }

            DesignTextField(
                state = descriptionState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                hasError = hasError && descriptionState.text.isEmpty(),
                lineLimits = TextFieldLineLimits.MultiLine(),
                error = {
                    if (descriptionState.text.isEmpty()) {
                        Text("Description cannot be empty")
                    }
                }
            ) {
                Text("Description")
            }
        }

        if (bothFieldsFilled) {
            DesignButton(
                onClick = {  },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
            ){
                Text("Post")
            }
        }
    }
}