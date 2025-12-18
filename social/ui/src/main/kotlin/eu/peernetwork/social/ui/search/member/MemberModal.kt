package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.social.ui.model.UiMember

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberModal(
    postLimit: Int,
    showSheet: MutableState<Boolean>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onClick: (UiMember) -> Boolean,
) {
    val state = remember { TextFieldState() }
    val focus = remember { FocusRequester() }
    val handleClick by rememberUpdatedState(onClick)
    DesignOverlay(
        state = showSheet,
        onDismiss = { showSheet.value = false }
    ) {
        MemberModal(state, showSheet, focus) {
            MemberScreen(
                state,
                postLimit,
                {
                    val shouldDismiss = handleClick(it)
                    if (shouldDismiss) {
                        showSheet.value = false
                    }
                    shouldDismiss
                },
                provider,
                viewModelStoreOwner
            )
        }
        LaunchedEffect(Unit) {
            focus.requestFocus()
        }
    }
}

@Composable
fun MemberModal(
    state: TextFieldState,
    enable: State<Boolean>,
    focusRequester: FocusRequester = FocusRequester(),
    content: @Composable () -> Unit
) {
    val updateContent by rememberUpdatedState(content)
    Box(modifier = Modifier
        .statusBarsPadding()
        .padding(vertical = 16.dp)) {
        Box(modifier = Modifier.padding(top = 48.dp)) {
            updateContent()
        }
        DesignTextField(
            state = state,
            enabled = enable.value,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            hint = stringResource(R.string.search_label),
            contentPadding = PaddingValues(vertical = 16.dp),
            minLines = 1,
            leading = {
                Text(
                    "@",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(
                        start = 14.dp,
                        end = 8.dp
                    )
                )
            },
            modifier = Modifier.focusRequester(focusRequester)
                .padding(horizontal = 18.dp)
        )
    }
}

@Preview
@Composable
fun PreviewMemberDialog() {
    DesignTheme(isDarkMode = true) {
        val state = remember { TextFieldState() }
        val enable = remember { mutableStateOf(true) }
        MemberModal(state, enable) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background))
        }
    }
}
