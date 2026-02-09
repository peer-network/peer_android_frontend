package eu.peernetwork.blog.ui.creator

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.extension.isValidInput

@Composable
fun CreatorFooter(
    title: TextFieldState,
    description: TextFieldState,
    enabled: State<Boolean>,
    isLoading: State<Boolean>,
    onSubmit: (UiDraft.Field) -> Unit = {},
) {
    val handleOnSubmit by rememberUpdatedState(onSubmit)
    val isFormValid = remember(title, description) { derivedStateOf {
        title.isValidInput() && (description.isValidInput()) || (enabled.value && title.isValidInput())
    } }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Spacer(modifier = Modifier.weight(1f))
        DesignButton(
            onClick = {
                handleOnSubmit(UiDraft.Field(
                    title.text.toString(),
                    description.text.toString())
                ) },
            isLoading = isLoading.value,
            enabled = isFormValid.value && !isLoading.value,
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 36.dp
            ),
            colors = designSecondaryButtonColors(),
            modifier = Modifier.height(42.dp),
        ) {
            Text(
                text = stringResource(eu.peernetwork.feature.blog.ui.R.string.post_label),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
