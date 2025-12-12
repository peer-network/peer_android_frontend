package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.design.material.DesignLabel
import eu.peernetwork.core.ui.theme.DesignTheme

@Composable
fun CreatorPage(
    title: TextFieldState,
    description: TextFieldState,
    isLoading: State<Boolean>,
    focus: FocusRequester,
    enabled: State<Boolean>,
    error: State<String?>,
    modifier: Modifier = Modifier,
    onSubmit: (UiDraft.Field) -> Unit = { },
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .padding(top = 8.dp)
    ) {
        DesignLabel(
            label = { error.value?.let {
                Text(
                    it,
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }},
            visible = error.value != null,
            modifier = Modifier.padding(bottom = 4.dp)
        ) { CreatorForm(title, focus, description, isLoading) }
        Spacer(modifier = Modifier.height(8.dp))
        CreatorFooter(
            title = title,
            description = description,
            isLoading = isLoading,
            enabled = enabled,
            onSubmit = onSubmit,
        )
    }
}

@Preview
@Composable
fun PreviewCreatorPage() {
    DesignTheme(isDarkMode = true) {
        val focus = remember { FocusRequester() }
        var title by rememberSaveable(stateSaver = TextFieldState.Saver) { mutableStateOf(TextFieldState()) }
        var description by rememberSaveable(stateSaver = TextFieldState.Saver) {
            mutableStateOf(TextFieldState())
        }
        CreatorPage(
            title = title,
            description = description,
            focus = focus,
            isLoading = remember { mutableStateOf(false) },
            enabled = remember { mutableStateOf(false) },
            error = remember { mutableStateOf(null) },
        ) {}
    }
}
