package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.model.UiMember

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberDialog(
    postLimit: Int,
    showSheet: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Boolean,
) {
    val state = remember { TextFieldState() }
    val focus = remember { FocusRequester() }
    DesignDialogSheet(
        tag = "MemberDialog",
        visible = showSheet,
        onAnimationComplete = {
            if (it) {
                focus.requestFocus()
            }
        }
    ) {
        MemberDialog(state, showSheet, focus) {
            MemberScreen(
                state,
                postLimit,
                onClick,
                provider,
                viewModelStoreOwner
            )
        }
    }
}

@Composable
fun MemberDialog(
    state: TextFieldState,
    enable: State<Boolean>,
    focusRequester: FocusRequester = FocusRequester(),
    content: @Composable () -> Unit
) {
    val updateContent by rememberUpdatedState(content)
    Column(modifier = Modifier
        .statusBarsPadding()
        .padding(vertical = 16.dp)) {
        DesignTextField(
            state,
            enabled = enable.value,
            focusRequester = focusRequester,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceDim,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceTint,
            ),
            leading = {
                Text(
                    "@",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.tertiary
                    ),
                    modifier = Modifier.padding(end = 8.dp)
                )
            },
            modifier = Modifier.padding(horizontal = 24.dp)
        ) { Text(stringResource(R.string.search_label)) }
        updateContent()
    }
}

@Preview
@Composable
fun PreviewMemberDialog() {
    PeerTheme {
        val state = remember { TextFieldState() }
        val enable = remember { mutableStateOf(true) }
        MemberDialog(state, enable) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
