package eu.peernetwork.app.ui.onboarding

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.design.material.DesignButton
import eu.peernetwork.core.ui.design.material.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.launch

enum class OnboardingFooterState {
    BEGIN,
    PROGRESS,
    COMPLETE
}

@Composable
fun OnboardingFooter(
    state: PagerState,
    isLoading: State<Boolean>,
    modifier: Modifier = Modifier,
    onFinish: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val derivedState = remember {
        derivedStateOf {
            when (state.currentPage) {
                0 -> OnboardingFooterState.BEGIN
                state.pageCount - 1 -> OnboardingFooterState.COMPLETE
                else -> OnboardingFooterState.PROGRESS
            }
        }
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp))
        OnboardingFooterIndicator(pagerState = state)
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp))
        OnboardingFooter(
            state = derivedState,
            isLoading = isLoading,
            onPreviousClick = {
                if (derivedState.value != OnboardingFooterState.BEGIN) {
                    scope.launch {
                        state.animateScrollToPage(state.currentPage - 1)
                    }
                }
            },
            onNextClick = {
                if (derivedState.value != OnboardingFooterState.COMPLETE) {
                    scope.launch {
                        state.animateScrollToPage(state.currentPage + 1)
                    }
                }
            },
            onFinish = onFinish
        )
    }
}

@Composable
fun OnboardingFooter(
    state: State<OnboardingFooterState>,
    isLoading: State<Boolean>,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onFinish: (Boolean) -> Unit
) {
    val handleOnFinish by rememberUpdatedState(onFinish)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
    ) {
        val isFinished = remember { derivedStateOf { state.value == OnboardingFooterState.COMPLETE } }
        AnimatedVisibility(
            visible = !isFinished.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OnboardingFooter(
                isLoading = isLoading,
                onNextClick = onNextClick,
                onSkipClick = { handleOnFinish(false) },
                onPreviousClick = if (state.value != OnboardingFooterState.BEGIN) {
                    onPreviousClick
                } else {
                    null
                }
            )
        }
        AnimatedVisibility(
            visible = isFinished.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            DesignButton(
                onClick = { handleOnFinish(true) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                enabled = !isLoading.value,
                isLoading = isLoading.value,
                shape = CircleShape,
                minHeight = 42.dp,
            ) { Text(
                text = stringResource(R.string.finish_label),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            ) }
        }
    }
}

@Composable
fun OnboardingFooter(
    isLoading: State<Boolean>,
    onPreviousClick: (() -> Unit)? = null,
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit
) {
    val handleOnPreviousClick by rememberUpdatedState(onPreviousClick)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        DesignOutlinedButton(
            onClick = onSkipClick,
            enabled = !isLoading.value,
            isLoading = isLoading.value,
            shape = CircleShape,
            textStyle = MaterialTheme.typography.labelLarge,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                disabledContainerColor = Color.Transparent
            ),
            minHeight = 38.dp,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
        ) { Text(stringResource(R.string.skip_label)) }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(
            visible = handleOnPreviousClick != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row {
                IconButton(
                    onClick = { handleOnPreviousClick?.invoke() },
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.primary),
                ) {
                    Image(
                        painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_back_arrow),
                        contentDescription = stringResource(R.string.back_label),
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
        IconButton(
            onClick = onNextClick,
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Image(
                painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_proceed),
                contentDescription = stringResource(R.string.proceed_label),
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun OnboardingFooterIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    dotCount: Int = pagerState.pageCount,
    size: Dp = 6.dp,
    spacing: Dp = 8.dp,
    activeColor: Color = MaterialTheme.colorScheme.onBackground,
    inactiveColor: Color = MaterialTheme.colorScheme.surfaceDim,
    animationDuration: Int = 300
) {
    Box(
        modifier = modifier
            .wrapContentWidth()
            .height(size),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(dotCount) {
                Box(
                    modifier = Modifier
                        .size(size)
                        .background(
                            color = inactiveColor,
                            shape = CircleShape
                        )
                )
            }
        }
        val animatedOffset by animateFloatAsState(
            targetValue = pagerState.currentPage + pagerState.currentPageOffsetFraction,
            animationSpec = tween(durationMillis = animationDuration, easing = EaseOutCubic),
            label = "indicator_offset"
        )
        Box(
            modifier = Modifier
                .size(size)
                .offset(((spacing + size) * animatedOffset))
                .background(
                    color = activeColor,
                    shape = CircleShape
                )
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun OnboardingFooterPreview() {
    PeerTheme {
        Column(modifier = Modifier.padding(
            vertical = 8.dp,
            horizontal = 24.dp
        )) {
            val state = remember { mutableStateOf(OnboardingFooterState.COMPLETE) }
            val isLoading = remember { mutableStateOf(false) }
            val pagerState = rememberPagerState(initialPage = 0, pageCount = { 6 })
            OnboardingFooter(pagerState, isLoading) {}
            OnboardingFooter(state, isLoading) {}
        }
    }
}
