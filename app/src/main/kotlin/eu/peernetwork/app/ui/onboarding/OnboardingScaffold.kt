package eu.peernetwork.app.ui.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun OnboardingScaffold(
    state: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceContainerLow
                    )
                )
            ).statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(eu.peernetwork.app.R.string.logo),
            modifier = Modifier.padding(horizontal = 22.dp)
                .height(48.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalPager(
            state = state,
            modifier = Modifier.weight(1f),
        ) { page -> updatedContent(page) }
        OnboardingFooter(state, Modifier.padding(
            horizontal = 24.dp
        ).padding(bottom = 8.dp)) { }
    }
}

@Composable
fun OnboardingContentScaffold(
    title: AnnotatedString,
    modifier: Modifier = Modifier,
    description: String? = null,
    slogan: AnnotatedString? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        description?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Box(
            content = content,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
        )
        slogan?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Normal
                ),
                color = MaterialTheme.colorScheme.surfaceTint,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
    }
}

@Composable
@Preview(name = "scaffold", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingScaffoldPreview() {
    PeerTheme {
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
        OnboardingScaffold(pagerState) {}
    }
}

@Composable
@Preview(name = "content scaffold", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingContentScaffoldPreview() {
    PeerTheme {
        val title = buildAnnotatedString {
            append("How ")
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                append("peer")
            }
            append(" works?")
        }
        val description = stringResource(R.string.onboarding_about)
        val slogan = stringResource(R.string.onboarding_about_Gems)
        OnboardingContentScaffold(
            title = title,
            description = description,
            slogan = slogan.annotate(),
            modifier = Modifier.padding(24.dp)
        ) { }
    }
}
