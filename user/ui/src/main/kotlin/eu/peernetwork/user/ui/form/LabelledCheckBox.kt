package eu.peernetwork.user.ui.form

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignCheckbox
import eu.peernetwork.user.ui.R

@Composable
fun LabelledCheckBox(
    state: MutableState<Boolean>,
    label: AnnotatedString,
    modifier: Modifier = Modifier,
    description: String? = label.text,
    textLayoutResult: MutableState<TextLayoutResult?> = remember { mutableStateOf(null) }
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DesignCheckbox(
            checked = state.value,
            onCheckedChange = { state.value = it },
            modifier = Modifier.padding(end = 8.dp),
            border = MaterialTheme.colorScheme.scrim
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = description,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = label,
            color = MaterialTheme.colorScheme.scrim,
            style = MaterialTheme.typography.labelMedium,
            onTextLayout = { textLayoutResult.value = it }
        )
    }
}
