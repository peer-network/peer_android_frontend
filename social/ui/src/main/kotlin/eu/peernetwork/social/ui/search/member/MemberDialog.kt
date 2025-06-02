package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOverlay
import eu.peernetwork.core.ui.design.compose.DesignTextField
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.model.UiMember

@Composable
fun MemberDialog(
    postLimit: Int,
    showSheet: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Unit,
) {
    val state = remember { TextFieldState() }
    MemberDialog(state, showSheet) {
        MemberScreen(
            state,
            postLimit,
            onClick,
            provider,
            viewModelStoreOwner
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberDialog(
    state: TextFieldState,
    showSheet: MutableState<Boolean>,
    content: @Composable () -> Unit
) {
    val focus = remember { FocusRequester() }
    val updateContent by rememberUpdatedState(content)
    DesignBottomSheet(tag = "MemberDialog", showSheet = showSheet) {
        Column {
            DesignTextField(
                state,
                focusRequester = focus,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
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
            ) { Text(stringResource(R.string.search_label)) }
            updateContent()
        }
    }
}

@Preview
@Composable
fun PreviewMemberDialog() {
    PeerTheme {
        val state = remember { TextFieldState() }
        val showSheet = remember { mutableStateOf(true) }
        DesignOverlay {
            MemberDialog(state, showSheet) {
                Box(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
