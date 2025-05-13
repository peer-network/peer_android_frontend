package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SetupHeader(
    state: MutableIntState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = if (isSystemInDarkTheme() || !BuildConfig.USE_SYSTEM_THEME) {
                painterResource(id = R.drawable.ic_logo)
            } else {
                painterResource(id = R.drawable.ic_logo_dark)
            },
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .padding(top = 24.dp)
                .height(56.dp)
        )
        Text(
            text = stringResource(id = R.string.slogan_text)
                .annotate(stringResource(id = R.string.app_name), SpanStyle(
                    fontWeight = FontWeight.Bold
                )),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            modifier = Modifier
                .padding(top = 24.dp, bottom = 8.dp)
                .padding(horizontal = 24.dp)
        )
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            TextButton(
                onClick = { state.intValue = 0 },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Box {
                    Text(
                        text = stringResource(id = eu.peernetwork.user.ui.R.string.login_text).lowercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.graphicsLayer {
                            alpha = if (state.intValue == 0) 0f else 1f
                        }
                    )
                    Text(
                        text = stringResource(id = eu.peernetwork.user.ui.R.string.login_text).lowercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.graphicsLayer {
                            alpha = if (state.intValue == 0) 1f else 0f
                        }
                    )
                }
            }
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(16.dp)
                    .background(MaterialTheme.colorScheme.onSurface)
                    .align(Alignment.CenterVertically)
            )
            TextButton(
                onClick = { state.intValue = 1 },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Box {
                    Text(
                        text = stringResource(id = eu.peernetwork.user.ui.R.string.register_text).lowercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.graphicsLayer {
                            alpha = if (state.intValue == 0) 1f else 0f
                        }
                    )
                    Text(
                        text = stringResource(id = eu.peernetwork.user.ui.R.string.register_text).lowercase(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.graphicsLayer {
                            alpha = if (state.intValue == 0) 0f else 1f
                        }
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupHeader() {
    PeerTheme {
        SetupHeader(state = rememberSaveable { mutableIntStateOf(0) })
    }
}
