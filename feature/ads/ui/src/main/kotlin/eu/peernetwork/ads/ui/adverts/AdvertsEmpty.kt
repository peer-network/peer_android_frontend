package eu.peernetwork.ads.ui.adverts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.feature.ads.ui.R

@Composable
fun AdvertsEmpty(onClick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 72.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.promotion_text),
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
        )
        DesignButton(
            onClick = onClick,
            minHeight = 42.dp,
            contentPadding = PaddingValues(
                vertical = 12.dp,
                horizontal = 24.dp
            ),
            colors = designSecondaryButtonColors(),
            modifier = Modifier.padding(top = 10.dp),
            trailing = {
                Icon(
                    painter = painterResource(R.drawable.ic_right),
                    contentDescription = stringResource(R.string.promotion_text),
                    modifier = Modifier.padding(start = 8.dp)
                        .size(22.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.scrim
                )
            }
        ) { Text(stringResource(R.string.view_posts)) }
    }
}

@Preview
@Composable
fun PreviewAdvertsEmpty() {
    DesignTheme(isDarkMode = true) {
        AdvertsEmpty {}
    }
}
