package eu.peernetwork.app.ui.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
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
    data class Action(
        val dailyFreeActions: Map<String, Int>,
        val actionTokenPrices: Map<String, Int>
    ) : OnboardingGuideState
    data class Diagram(
        val dailyNumberToken: Int,
        val formatted: String
    ) : OnboardingGuideState
    data class Engagement(
        val dailyNumberToken: Int,
        val formatted: String,
        val actionGemsReturns: Map<String, Double>
    ) : OnboardingGuideState
    data object Feature : OnboardingGuideState
}

@Composable
fun OnboardingGuide(
    state: OnboardingGuideState,
    modifier: Modifier = Modifier
) {
    when (state) {
        is OnboardingGuideState.Introduction -> OnboardingIntroductionGuide(modifier)
        is OnboardingGuideState.Action -> OnboardingActionGuide(
            dailyFreeActions = state.dailyFreeActions,
            actionTokenPrices = state.actionTokenPrices,
            modifier = modifier
        )
        is OnboardingGuideState.Diagram -> OnboardingDiagramGuide(
            formatted = state.formatted,
            modifier = modifier
        )
        is OnboardingGuideState.Engagement -> OnboardingEngagementGuide(
            formatted = state.formatted,
            actionGemsReturns = state.actionGemsReturns,
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
    ) {
        InteractionCycle(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun OnboardingActionGuide(
    dailyFreeActions: Map<String, Int>,
    actionTokenPrices: Map<String, Int>,
    modifier: Modifier = Modifier
) {
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
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.onboarding_want_more),
                color = MaterialTheme.colorScheme.tertiary,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(12.dp))
            OnboardingActionListing(actionTokenPrices = actionTokenPrices)
        }
    }
}

@Composable
private fun OnboardingDiagramGuide(
    formatted: String,
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
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            RewardDiagram(text = formatted)
        }
    }
}

@Composable
private fun OnboardingEngagementGuide(
    formatted: String,
    actionGemsReturns: Map<String, Double>,
    modifier: Modifier = Modifier
) {
    val title = stringResource(R.string.onboarding_engage_earn)
    val slogan = stringResource(R.string.onboarding_gems_collect_explanation, formatted)
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
            OnboardingEngagementListing(actionGemsReturns = actionGemsReturns)
        }
    }
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
            dailyFreeActions = mapOf(
                "post" to 1,
                "like" to 3,
                "comment" to 4
            ),
            actionTokenPrices = mapOf(
                "post" to 20,
                "like" to 3,
                "comment" to 1,
                "dislike" to 3
            ),
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun DiagramOnboardingGuidePreview() {
    PeerTheme {
        OnboardingDiagramGuide(
            formatted = "5 000",
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun EngageOnboardingGuidePreview() {
    PeerTheme {
        OnboardingEngagementGuide(
            formatted = "5,000",
            actionGemsReturns = mapOf(
                "like" to 5.0,
                "dislike" to -3.0,
                "comment" to 2.0,
                "view" to 0.25
            ),
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
