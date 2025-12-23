package eu.peernetwork.core.ui.design.material

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import eu.peernetwork.core.ui.theme.DesignTheme

interface DesignDropDownBuilder {
    fun item(
        tag: String,
        onClick: (() -> Boolean)? = null,
        content: @Composable (String, Boolean) -> Unit
    )
}

data class DesignDropDownItem(
    val tag: String,
    val onClick: (() -> Boolean)? = null,
    val content: @Composable (String, Boolean) -> Unit
)

@Composable
fun DesignDropDown(
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    default: String,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = RoundedCornerShape(28),
    content: DesignDropDownBuilder.() -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    var size by remember { mutableStateOf(IntSize.Zero) }
    val current = remember { mutableStateOf<DesignDropDownItem?>(null) }
    val factory = remember { mutableStateMapOf<String, DesignDropDownItem>() }
    val items = remember { mutableStateMapOf<Int, DesignDropDownItem>() }
    val builder = remember { object : DesignDropDownBuilder {
        override fun item(
            tag: String,
            onClick: (() -> Boolean)?,
            content: @Composable ((String, Boolean) -> Unit)
        ) {
            DesignDropDownItem(tag, onClick, content).let {
                factory[tag] = it
                items[items.size] = it
            }
        }
    } }
    Box(modifier = Modifier.clip(shape)
        .background(color = color)
        .onGloballyPositioned { coordinates ->
            size = coordinates.size
        }
    ) { current.value?.let {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = { state.value = !state.value })
        ) { it.content(it.tag, true) }
    } ?: factory[default]?.let {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = { state.value = !state.value })
        ) { it.content(it.tag, true) }
    } }
    if (state.value) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(x = 0, y = size.height),
            onDismissRequest = { state.value = false },
            properties = PopupProperties(focusable = true)
        ) {
            Column(modifier = Modifier.width(IntrinsicSize.Max).then(modifier)) {
                items.forEach { item ->
                    key(item.key) {
                        Box(modifier = Modifier.fillMaxWidth()
                            .clickable(
                                role = Role.Button,
                                onClick = {
                                    if (item.value.onClick == null
                                        || item.value.onClick?.invoke() == true) {
                                        current.value = item.value
                                        state.value = false
                                    }
                                }
                            ).padding(contentPadding)
                        ) { item.value.content(item.value.tag, false) }
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        updatedContent(builder)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignDropDown() {
    DesignTheme(isDarkMode = true) {
        val expanded = remember { mutableStateOf(false) }
        DesignDropDown(
            expanded,
            default = "tag",
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary),
        ) {
            item(tag = "tag", { true }) { label, isActive ->
                Text(
                    label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
            item(tag = "tag1", { true }) { label, isActive ->
                Text(
                    label,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        }
    }
}
