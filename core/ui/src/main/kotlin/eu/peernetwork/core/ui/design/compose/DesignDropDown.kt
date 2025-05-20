package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

interface DesignDropDownBuilder {
    fun item(
        tag: String,
        onClick: (() -> Boolean)? = null,
        content: @Composable (String) -> Unit
    )
}

data class DesignDropDownItem(
    val tag: String,
    val onClick: (() -> Boolean)? = null,
    val content: @Composable (String) -> Unit
)

@Composable
fun DesignDropDown(
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    default: String,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    shape: Shape = RoundedCornerShape(28),
    content: DesignDropDownBuilder.() -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    var position by remember { mutableStateOf<Offset>(Offset.Zero) }
    var size by remember { mutableStateOf<IntSize>(IntSize.Zero) }
    val current = remember { mutableStateOf<DesignDropDownItem?>(null) }
    val factory = remember { mutableStateMapOf<String, DesignDropDownItem>() }
    val builder = remember { object : DesignDropDownBuilder {
        override fun item(
            tag: String,
            onClick: (() -> Boolean)?,
            content: @Composable ((String) -> Unit)
        ) { factory[tag] = DesignDropDownItem(tag, onClick, content) }
    } }
    Box(modifier = Modifier.clip(shape)
        .background(color = color)
        .onGloballyPositioned { coordinates ->
            size = coordinates.size
            position = coordinates.localToWindow(Offset.Zero)

        }
    ) { current.value?.let {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = { state.value = !state.value })
        ) { it.content(it.tag) }
    } ?: factory[default]?.let {
        Box(modifier = Modifier.clickable(
            role = Role.Button,
            onClick = { state.value = !state.value })
        ) { it.content(it.tag) }
    } }
    if (state.value) {
        Popup(
            alignment = Alignment.TopStart,
            offset = IntOffset(x = 0, y = size.height),
            onDismissRequest = { state.value = false },
            properties = PopupProperties(focusable = true)
        ) {
            Column(modifier = modifier.then(Modifier.width(IntrinsicSize.Max))) {
                factory.forEach { item ->
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
                            )
                        ) { item.value.content(item.value.tag) }
                    }
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        updatedContent(builder)
    }
}
