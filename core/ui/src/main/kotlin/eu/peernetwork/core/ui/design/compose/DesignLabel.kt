package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun DesignLabel(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    visible: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    content: @Composable (BoxScope.() -> Unit),
) {
    val updatedLabel by rememberUpdatedState(label)
    val updatedContent by rememberUpdatedState(content)
    ConstraintLayout(modifier = modifier) {
        val (contentTag, labelTag) = createRefs()
        Box(modifier = Modifier.constrainAs(contentTag) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            if (visible) {
                bottom.linkTo(labelTag.top)
            }
        }) { updatedContent() }
        AnimatedVisibility(
            visible = visible,
            modifier = Modifier.constrainAs(labelTag) {
                top.linkTo(contentTag.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
        }) {
            updatedLabel?.run {
                CompositionLocalProvider(LocalTextStyle provides textStyle) { this() }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignLabel() {
    PeerTheme {
        Column {
            DesignLabel(
                label = { Text( text = "Label") }
            ) {
                Text(
                    text = "Hello, world!",
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright)
                )
            }
            DesignLabel(
                visible = false,
                label = { Text( text = "Label") },
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Text(
                    text = "Hello, world!",
                    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceBright)
                )
            }
        }
    }
}
