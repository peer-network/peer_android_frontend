package eu.peernetwork.app.ui.onboarding

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.R
import androidx.compose.foundation.background as fbBackground

@Composable
fun OnboardingPageTwo(
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
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = (-6).dp)
                )
                Column(modifier = Modifier.offset(y = (-19).dp)) {
                    Text(
                        text = stringResource(R.string.onboarding_create_like_comment)+ "\n\n"+ stringResource(R.string.onboarding_smartly),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.size(15.dp))
                    Text(
                        text = stringResource(R.string.onboarding_daily_free_pass),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.size(3.dp))

            Surface(
                shape = RoundedCornerShape(14.dp),
                tonalElevation = 30.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 8.dp)
                    .padding(vertical = 2.dp)
                    .offset(y = (-19).dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 13.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera_outline),
                            contentDescription = "1 post",
                            modifier = Modifier
                                .size(55.dp)
                                .offset(x = (-11).dp, y = (-2).dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = stringResource(R.string.onboarding_post_count),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.offset(x = (-11).dp, y = (-2).dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (-8).dp)
                            .width(1.dp)
                            .heightIn(min = 60.dp)
                            .fbBackground(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_redheart),
                            contentDescription = "3 likes",
                            modifier = Modifier
                                .size(35.dp)
                                .offset(x = (-5).dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.onboarding_like_count),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.offset(x = (-2).dp, y = 13.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .heightIn(min = 60.dp)
                            .fbBackground(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_chat_out),
                            contentDescription = "4 comments",
                            modifier = Modifier
                                .size(32.dp)
                                .offset(x = 5.dp),
                            contentScale = ContentScale.Fit
                        )
                        Text(
                            text = stringResource(R.string.onboarding_comment_count),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.offset(x = 3.dp, y = 15.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.onboarding_want_more),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .offset(x = 3.dp, y = (-17).dp)
                )

                OptionRow(
                    leadingIconRes = R.drawable.ic_camera_outline,
                    label = stringResource(R.string.onboarding_option_extra_post),
                    amount = "20",
                    amountIconRes = R.drawable.ic_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .offset(y = (-10).dp)
                )

                OptionRow(
                    leadingIconRes = R.drawable.ic_like_outline,
                    label = stringResource(R.string.onboarding_option_extra_like),
                    amount = "3",
                    amountIconRes = R.drawable.ic_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .offset(y = (-14).dp)
                )

                OptionRow(
                    leadingIconRes = R.drawable.ic_chat_out,
                    label = stringResource(R.string.onboarding_option_extra_comments),
                    amount = "1",
                    amountIconRes = R.drawable.ic_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .offset(y = (-18).dp)
                )

                OptionRow(
                    leadingIconRes = R.drawable.ic_dislike_outline,
                    label = stringResource(R.string.onboarding_option_dislike),
                    amount = "3",
                    amountIconRes = R.drawable.ic_icon,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .offset(y = (-22).dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(R.string.onboarding_invite_friends_prefix))
                        append(" ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.onboarding_invite_friends_bold))
                        }
                        append(" ")
                        append(stringResource(R.string.onboarding_invite_friends_suffix))
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .offset(y = (-18).dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.offset(y = (-20).dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = true)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
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
                            .offset(y = (-23).dp),
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
                                .offset(y = (-21).dp, x = 220.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_back_arrow),
                                contentDescription = stringResource(R.string.onboarding_back),
                                modifier = Modifier.size(28.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    IconButton(
                        onClick = onNext,
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.CenterEnd)
                            .offset(y = (-23).dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_proceed_arrow),
                            contentDescription = stringResource(R.string.onboarding_next),
                            modifier = Modifier.size(28.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionRow(
    leadingIconRes: Int,
    label: String,
    amount: String,
    amountIconRes: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(46.dp)
            .clip(RoundedCornerShape(144.dp))
            .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f))
            .padding(horizontal = 1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                tonalElevation = 400.dp,
                modifier = Modifier.size(44.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .fbBackground(Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(id = leadingIconRes),
                        contentDescription = label,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = amount,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.offset(x = (-11).dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
                contentDescription = stringResource(eu.peernetwork.user.ui.R.string.about_us_label),
                modifier = Modifier
                    .size(25.dp)
                    .offset(x = (-11).dp, y = (-1).dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}