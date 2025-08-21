package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignCollapsibleText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean> = rememberSaveable { mutableStateOf(false) },
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    style: TextStyle = LocalTextStyle.current,
    onClick: (Int) -> Unit = {}
) {

    val delegate by rememberUpdatedState(onClick)
    val toggleOrDelegate: (Int) -> Unit = { offset ->
        val hasSpan = text.getStringAnnotations(offset, offset).isNotEmpty()
        if (hasSpan) delegate(offset) else expanded.value = !expanded.value
    }

    Layout(
        modifier = modifier,
        content = {
            ClickableText(
                text = text,
                maxLines = maxLines,
                overflow = overflow,
                style = style,
                onClick = toggleOrDelegate
            )
            ClickableText(
                text = text,
                maxLines = maxLines,
                overflow = overflow,
                style = style,
                onClick = toggleOrDelegate
            )
            ClickableText(text = text, onClick = toggleOrDelegate, style = style)
            Text(
                text = if (!expanded.value) {
                    stringResource(R.string.show_less)
                } else {
                    stringResource(R.string.show_more)
                },
                modifier = Modifier.clickable { expanded.value = !expanded.value },
                style = style.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    ) { measurables, constraints ->
        val labelMeasurable = measurables[3]
        val labelPlaceable = labelMeasurable.measure(constraints)
        val placeholderMeasurable = measurables[1].measure(constraints.copy(
            maxWidth = constraints.maxWidth - labelPlaceable.width
        ))
        val placeable = measurables[0].measure(constraints)
        val fullPlaceable = measurables[2].measure(constraints.copy(
            maxHeight = Int.MAX_VALUE
        ))
        val width = constraints.maxWidth
        val height = if (expanded.value) {
            fullPlaceable.height + labelPlaceable.height
        } else {
            placeable.height
        }
        layout(width, height) {
            if (expanded.value) {
                fullPlaceable.place(0, 0)
                if (fullPlaceable.height > placeable.height) {
                    labelPlaceable.place(0, fullPlaceable.height)
                }
            } else {
                if (fullPlaceable.height > placeable.height) {
                    placeholderMeasurable.place(0, 0)
                    labelPlaceable.place(placeholderMeasurable.width, placeholderMeasurable.height - labelPlaceable.height)
                } else {
                    placeable.place(0, 0)
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAttachmentPreview() {
    PeerTheme {
        Column {
            DesignCollapsibleText(
                buildAnnotatedString {
                    append("Hello, world!")
                },
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            DesignCollapsibleText(
                buildAnnotatedString {
                    append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse fringilla, dui nec viverra pretium, magna nisl congue turpis, a elementum turpis enim in lacus.")
                },
                maxLines = 1,
            )
        }
    }
}
