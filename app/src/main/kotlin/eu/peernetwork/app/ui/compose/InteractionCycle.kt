package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun BoxScope.InteractionCycle(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .align(Alignment.Center),
        contentAlignment = contentAlignment
    ) {
        ProcessCycle(modifier = Modifier.fillMaxWidth(fraction = .9f)
            .widthIn(max = 280.dp)
            .aspectRatio(1f)
            .then(modifier)) {
            IconLabel(
                painter = painterResource(id = R.drawable.bg_heart),
                label = stringResource(R.string.engage),
                modifier = Modifier.align(Alignment.TopCenter)
            )
            IconLabel(
                painter = painterResource(id = R.drawable.bg_camera),
                label = stringResource(R.string.create),
                modifier = Modifier.align(Alignment.CenterEnd)
            )
            IconLabel(
                painter = painterResource(id = R.drawable.bg_coins),
                label = stringResource(R.string.earn),
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            IconLabel(
                painter = painterResource(id = R.drawable.bg_repeat),
                label = stringResource(R.string.repeat),
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun InteractionCyclePreview() {
    PeerTheme {
        Box { InteractionCycle() }
    }
}
