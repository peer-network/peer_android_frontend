package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import eu.peernetwork.core.ui.R

@Composable
fun OnboardingPageOne(
    onSkip: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f)
                    )
                )
            )
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Peer logo",
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.TopStart)
                        .offset(x = (-6).dp)
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(y = 71.dp)
                        .zIndex(1f)
                ) {
                    val title = buildAnnotatedString {
                        append("How ")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                            append("peer")
                        }
                        append(" works?")
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 30.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(11.dp))
                    Text(
                        text = stringResource(R.string.onboarding_about),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 220.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularIconClusterWithLabels()
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.onboarding_about_Gems),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-49).dp)
                )
                Row(
                    modifier = Modifier.offset(y = (-14).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Dot(active = true)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onSkip,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 8.dp)
                            .offset(y = (-15).dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.04f)
                        ),
                        contentPadding = PaddingValues(horizontal = 36.dp, vertical = 8.dp)
                    ) {
                        Text(text = "Skip", color = MaterialTheme.colorScheme.onBackground)
                    }
                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(45.dp)
                            .offset(y = (-15).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_proceed_arrow),
                            contentDescription = "Proceed",
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun Dot(active: Boolean) {
    val size = if (active) 8.dp else 6.dp
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.22f)
            )
    )
}

@Composable
private fun CircularIconClusterWithLabels() {
    val size = 280.dp
    Box(modifier = Modifier.size(size).offset(y = (-10).dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Engage",
                modifier = Modifier.size(70.dp)
            )
            Text(
                text = "Engage",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.CenterEnd).offset(x = (-6).dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_camera_blue),
                contentDescription = "Create",
                modifier = Modifier.size(70.dp).offset(y = 4.dp)
            )
            Text(
                text = "Create",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter).offset(y = 23.dp, x = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_coins),
                contentDescription = "Earn",
                modifier = Modifier.size(70.dp)
            )
            Text(
                text = "Earn",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.CenterStart).offset(x = 6.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_repeat),
                contentDescription = "Repeat",
                modifier = Modifier.size(70.dp)
            )
            Text(
                text = "Repeat",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f)
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_curvedarrow_top_left),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(55.dp)
                .offset(x = (-40).dp, y = 40.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_curvedarrow_bottom_left),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(55.dp)
                .offset(x = (-40).dp, y = (-34).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_curvedarrow_bottom_right),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(55.dp)
                .offset(x = 40.dp, y = (-34).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_curvedarrow_top_right),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(55.dp)
                .offset(x = 40.dp, y = 34.dp)
        )
    }
}