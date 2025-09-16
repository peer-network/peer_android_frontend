package eu.peernetwork.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.R

@Composable
fun OnboardingPageFive(
    onSkip: () -> Unit,
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
            .padding(horizontal = 20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = "Peer logo",
                    modifier = Modifier
                        .size(100.dp)
                        .offset(x = (-6).dp, y = 12.dp)
                )
                Text(
                    text = "How to use tokens?",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .offset(y = (-6).dp)
                        .padding(start = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = 60.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.Top
                    ) {
                        FeatureCard(
                            iconRes = R.drawable.ic_unlock_blue,
                            label = "Post and\nengage more"
                        )
                        FeatureCard(
                            iconRes = R.drawable.ic_ad,
                            label = "Boost your\ncontent",
                            info = "Coming soon..."
                        )
                    }
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.Top
                    ) {
                        FeatureCard(
                            iconRes = R.drawable.ic_shop,
                            label = "Shop in-app",
                            info = "Coming soon..."
                        )
                        FeatureCard(
                            iconRes = R.drawable.ic_btctransfer,
                            label = "Cash-out",
                            info = "We're working on the license - stay tuned!"
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.offset(y = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = false)
                    Spacer(modifier = Modifier.width(6.dp))
                    Dot(active = true)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onSkip,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 21.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Lets go!",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    iconRes: Int,
    label: String,
    info: String = ""
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White.copy(alpha = 0.07f),
        modifier = Modifier
            .width(140.dp)
            .height(150.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 18.dp, bottom = 14.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(30.dp)
                )
            }
            Spacer(modifier = Modifier.height(13.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
                maxLines = 2
            )
            if (info.isNotEmpty()) {
                Spacer(modifier = Modifier.height(7.dp))
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = Color.White.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    maxLines = 3
                )
            }
        }
    }
}
