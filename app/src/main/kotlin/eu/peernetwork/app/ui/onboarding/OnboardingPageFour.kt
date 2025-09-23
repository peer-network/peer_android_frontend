package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.R

@Composable
fun OnboardingPageFour(
    onSkip: () -> Unit,
    onBack: () -> Unit,
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Peer logo",
                    modifier = Modifier.size(100.dp).offset(x = (-6).dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.onboarding_your_effort_reward),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.offset(y = (-23).dp)
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = stringResource(R.string.onboarding_gems_slice_pool),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.offset(y = (-22).dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 260.dp)
                        .padding(bottom = 2.dp)
                        .offset(y = (-27).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_pie),
                        contentDescription = "Pie chart",
                        modifier = Modifier.size(300.dp),
                        contentScale = ContentScale.Fit
                    )

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "5 000",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 21.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                                contentDescription = "icon",
                                modifier = Modifier.size(22.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = stringResource(R.string.onboarding_pie_chart_distributed_daily),
                            modifier = Modifier.offset(y = (-6).dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Thin,
                                fontSize = 11.sp
                            ),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.onboarding_pie_chart_100_percent_of),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 11.sp
                                ),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gem),
                                contentDescription = "Gems",
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 11.sp
                                ),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.offset(x = 75.dp, y = (-75).dp)
                    )
                    Text(
                        text = "25%",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.offset(x = (-104).dp, y = 1.dp)
                    )
                    Text(
                        text = "25%",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.offset(x = 9.dp, y = 104.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(145.dp)
                        .offset(y = (-85).dp)
                ) {
                    VerticalLine(
                        Modifier
                            .offset(x = 24.dp, y = (-13).dp)
                            .height(100.dp)
                    )
                    VerticalLine(
                        Modifier
                            .align(Alignment.Center)
                            .height(105.dp)
                            .offset(y = 40.dp)
                    )
                    VerticalLine(
                        Modifier
                            .offset(x = 294.dp, y = (-13).dp)
                            .height(100.dp)
                    )

                    Column(
                        modifier = Modifier.offset(x = 21.dp, y = 90.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_user_a),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.onboarding_has_5),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gem),
                                contentDescription = "Gems",
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text(
                                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.onboarding_will_get_1250),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                                contentDescription = "icon",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .offset(y = 80.dp, x = 43.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_user_b),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text =stringResource(R.string.onboarding_has_5),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gem),
                                contentDescription = "Gems",
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text(
                                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.onboarding_will_get_1250),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                                contentDescription = "icon",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.offset(x = 201.dp, y = 90.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = stringResource(R.string.onboarding_user_c),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            textAlign = TextAlign.End
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(R.string.onboarding_has_10),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_gem),
                                contentDescription = "Gems",
                                modifier = Modifier.size(14.dp),
                                tint = Color.White
                            )
                            Text(
                                text = stringResource(R.string.onboarding_pie_chart_gems_label),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = stringResource(R.string.onboarding_will_get_2500),
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Icon(
                                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                                contentDescription = "icon",
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.offset(y = (-11).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = true)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                }

                Spacer(modifier = Modifier.height(14.dp))

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
                        Text(text = stringResource(R.string.onboarding_skip), color = MaterialTheme.colorScheme.onBackground)
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(45.dp)
                                .offset(y = (-12).dp, x = 220.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = stringResource(R.string.onboarding_back),
                                modifier = Modifier.size(28.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.CenterEnd)
                            .offset(y = (-14).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_proceed_arrow),
                            contentDescription = stringResource(R.string.onboarding_next),
                            modifier = Modifier.size(28.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun VerticalLine(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(2.dp)
            .fillMaxHeight()
            .background(Color.White.copy(alpha = 0.55f))
    )
}

