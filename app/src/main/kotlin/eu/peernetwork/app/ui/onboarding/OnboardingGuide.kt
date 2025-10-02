package eu.peernetwork.app.ui.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.app.ui.compose.DailyFreebies
import eu.peernetwork.app.ui.compose.InteractionCycle
import eu.peernetwork.app.ui.compose.PostSnapshot
import eu.peernetwork.app.ui.compose.RewardDiagram
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

sealed interface OnboardingGuideState {
    data object Introduction : OnboardingGuideState
    data object Action : OnboardingGuideState
    data class Diagram(val price: String) : OnboardingGuideState
    data class Engagement(val price: String) : OnboardingGuideState
    object Feature : OnboardingGuideState
}

@Composable
fun OnboardingGuide(
    state: OnboardingGuideState,
    modifier: Modifier = Modifier
) {
    when (state) {
        is OnboardingGuideState.Introduction -> OnboardingIntroductionGuide(modifier)
        is OnboardingGuideState.Action -> OnboardingActionGuide(modifier)
        is OnboardingGuideState.Diagram -> OnboardingDiagramGuide(
            price = state.price,
            modifier = modifier
        )
        is OnboardingGuideState.Engagement -> OnboardingEngagementGuide(
            price = state.price,
            modifier = modifier
        )
        is OnboardingGuideState.Feature -> OnboardingFeatureGuide(modifier)
    }
}

@Composable
private fun OnboardingIntroductionGuide(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.onboarding_how_title)
        .annotate(
            text = stringResource(id = R.string.label).lowercase(),
            style = SpanStyle(
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
        )
    val description = stringResource(R.string.onboarding_about)
    val slogan = stringResource(R.string.onboarding_about_Gems)
    OnboardingContentScaffold(
        title = title,
        description = description,
        slogan = slogan.annotate(),
        modifier = modifier
    ) { InteractionCycle(modifier = Modifier.align(Alignment.Center)) }
}

@Composable
private fun OnboardingActionGuide(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.onboarding_create_like_comment)
    val slug = stringResource(R.string.onboarding_smartly)
    val slogan = stringResource(R.string.onboarding_invite_friends_prefix).annotate(
        text = stringResource(R.string.onboarding_invite_friends_bold),
        style = SpanStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.SemiBold
        )
    )
    OnboardingContentScaffold(
        title = buildAnnotatedString {
            append(title)
            append("\n")
            append(slug)
        },
        slogan = slogan,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = stringResource(R.string.onboarding_daily_free_pass),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            DailyFreebies()
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.onboarding_want_more),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            OnboardingActionListing()
        }
    }
}

@Composable
private fun OnboardingDiagramGuide(
    price: String,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.onboarding_your_effort_reward)
        .annotate(
            text = "=",
            style = SpanStyle(color = MaterialTheme.colorScheme.tertiary)
        )
    val description = stringResource(R.string.onboarding_about)
    OnboardingContentScaffold(
        title = title,
        description = description,
        modifier = modifier
    ) { Box(modifier = Modifier.fillMaxSize()
        .padding(top = 16.dp)) {
        RewardDiagram(price)
    } }
}

@Composable
private fun OnboardingEngagementGuide(
    price: String,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.onboarding_engage_earn)
    val slogan = stringResource(R.string.onboarding_gems_collect_explanation, price)
    OnboardingContentScaffold(
        title = title.annotate(),
        slogan = slogan.annotate(),
        modifier = modifier
    ) { Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(12.dp))
        PostSnapshot()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.onboarding_want_more),
            color = MaterialTheme.colorScheme.tertiary,
            style = MaterialTheme.typography.labelLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingEngagementListing()
    } }
}

@Composable
fun OnboardingFeatureGuide(modifier: Modifier = Modifier) {
    val title = stringResource(R.string.onboarding_how_to_use_tokens)
    OnboardingContentScaffold(
        title = title.annotate(),
        modifier = modifier
    ) { OnboardingFeatureListing() }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun OnboardingGuidePreview() {
    PeerTheme {
        OnboardingIntroductionGuide(
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun HowOnboardingGuidePreview() {
    PeerTheme {
        OnboardingActionGuide(
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DiagramOnboardingGuidePreview() {
    PeerTheme {
        OnboardingDiagramGuide(
            price = "5 000",
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EngageOnboardingGuidePreview() {
    PeerTheme {
        OnboardingEngagementGuide(
            price = "5,000",
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun FeatureOnboardingGuidePreview() {
    PeerTheme {
        OnboardingFeatureGuide(
            modifier = Modifier.padding(24.dp)
        )
    }
}
