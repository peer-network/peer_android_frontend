package eu.peernetwork.app.ui.compose

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun RewardDiagram(text: String, modifier: Modifier = Modifier) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val width = maxWidth * .8f
        val verticalLineHeight = width * .35f
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxWidth()
                .padding(top = width * .7f)
        ) {
            Spacer(modifier = Modifier.width(1.dp)
                .height(verticalLineHeight)
                .background(MaterialTheme.colorScheme.onTertiaryContainer))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.width(width)
                    .padding(top = verticalLineHeight)
            ) {
                RewardLabel(
                    title = stringResource(R.string.onboarding_user_a),
                    description = stringResource(R.string.onboarding_has_5),
                    slug = stringResource(R.string.onboarding_will_get_1250)
                )
                RewardLabel(
                    title = stringResource(R.string.onboarding_user_c),
                    description = stringResource(R.string.onboarding_has_10),
                    slug = stringResource(R.string.onboarding_will_get_2500),
                    horizontalAlignment = Alignment.End
                )
            }
            Spacer(modifier = Modifier.width(1.dp)
                .height(verticalLineHeight)
                .background(MaterialTheme.colorScheme.onTertiaryContainer))
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.bg_pie),
                    contentDescription = stringResource(R.string.onboarding_your_effort_reward),
                    modifier = Modifier.size(width),
                    contentScale = ContentScale.Fit
                )
                RewardLabel(text)
            }
            Spacer(modifier = Modifier.padding(top = 8.dp)
                .width(1.dp)
                .height(verticalLineHeight)
                .background(MaterialTheme.colorScheme.onTertiaryContainer))
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(0.999f))
                Box(modifier = Modifier.weight(1.01f)) {
                    RewardLabel(
                        title = stringResource(R.string.onboarding_user_b),
                        description = stringResource(R.string.onboarding_has_5),
                        slug = stringResource(R.string.onboarding_will_get_1250),
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardLabel(text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                contentDescription = stringResource(eu.peernetwork.social.ui.R.string.peer_label),
                modifier = Modifier.size(12.dp),
                tint = Color.White
            )
        }
        Text(
            text = stringResource(R.string.onboarding_pie_chart_distributed_daily),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.onboarding_pie_chart_100_percent_of),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                painter = painterResource(id = eu.peernetwork.blog.ui.R.drawable.ic_gem),
                contentDescription = stringResource(R.string.onboarding_pie_chart_gems_label),
                modifier = Modifier.padding(horizontal = 4.dp)
                    .size(10.dp),
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}

@Composable
private fun RewardLabel(
    title: String,
    description: String,
    slug: String,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
) {
    Column(horizontalAlignment = horizontalAlignment) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
            Icon(
                painter = painterResource(id = eu.peernetwork.blog.ui.R.drawable.ic_gem),
                contentDescription = stringResource(R.string.onboarding_pie_chart_gems_label),
                modifier = Modifier.padding(horizontal = 4.dp)
                    .size(10.dp),
                tint = Color.White
            )
            Text(
                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = slug,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_icon),
                contentDescription = stringResource(eu.peernetwork.social.ui.R.string.peer_label),
                modifier = Modifier.size(12.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RewardDiagramPreview() {
    PeerTheme {
        Box(modifier = Modifier.fillMaxSize()
            .padding(24.dp)) {
            RewardDiagram("5 000")
        }
    }
}
