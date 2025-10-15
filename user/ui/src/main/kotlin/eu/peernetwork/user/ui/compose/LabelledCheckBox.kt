package eu.peernetwork.user.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignCheckbox
import eu.peernetwork.user.ui.R

@Composable
fun LabelledCheckBox(
    state: MutableState<Boolean>,
    label: String,
    modifier: Modifier = Modifier,
    description: String? = label,
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
            style = MaterialTheme.typography.labelMedium
        )
    }
}
