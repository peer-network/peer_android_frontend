package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import eu.peernetwork.core.ui.compose.FlexBox
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SetupScaffold(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    footer: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (headerTag, sectionTag, footerTag) = createRefs()
        createVerticalChain(headerTag, sectionTag, chainStyle = ChainStyle.Packed)
        Box(
            modifier = Modifier.constrainAs(headerTag) {
                top.linkTo(parent.top)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(sectionTag.top)
            },
        ) { header() }
        FlexBox(
            minHeight = 0.45f,
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.constrainAs(sectionTag) {
                top.linkTo(headerTag.bottom)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(footerTag.top)
            }
        ) { content() }
        Box(
            modifier = Modifier.constrainAs(footerTag) {
                top.linkTo(sectionTag.bottom)
                start.linkTo(parent.start, margin = 24.dp)
                end.linkTo(parent.end, margin = 24.dp)
                bottom.linkTo(parent.bottom, margin = 36.dp)
            }
        ) { footer() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupScaffold() {
    PeerTheme {
        SetupScaffold(
            header = {
                Text(
                    text = "Header",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            footer = {
                Text(
                    text = "Footer",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                text = "Content",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
