package eu.peernetwork.blog.ui.comment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    state: MutableState<String?>,
    modifier: Modifier = Modifier
) {
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    DesignBottomSheet(
        showSheet = showSheet,
        tag = "commentBottomSheet",
        modifier = modifier,
        onDismissRequest = { state.value = null },
        color = MaterialTheme.colorScheme.tertiaryContainer,
        sheetPeekHeight = 600.dp,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                ) {

                }
            }
        }
    )
}
