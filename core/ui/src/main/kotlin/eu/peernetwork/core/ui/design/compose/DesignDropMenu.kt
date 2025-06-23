package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun DesignDropdownMenu(
    modifier : Modifier = Modifier,
    items    : List<String>,
    onSelect : (String) -> Unit,
    anchor   : @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }


    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .clickable { expanded = true }
    ) { anchor() }

    DropdownMenu(
        expanded          = expanded,
        onDismissRequest  = { expanded = false }
    ) {
        items.forEach { label ->
            DropdownMenuItem(
                text    = { androidx.compose.material3.Text(label) },
                onClick = {
                    expanded = false
                    onSelect(label)
                }
            )
        }
    }
}
