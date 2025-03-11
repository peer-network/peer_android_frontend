package eu.peernetwork.app.ui.setup

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import eu.peernetwork.app.R
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun SetupHeader(
    onLogin: () -> Unit,
    onRegister: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val appName = stringResource(id = R.string.app_name)
        val (logo, slogan, divider, login, register) = createRefs()
        Image(
            painter = if (isSystemInDarkTheme()) {
                painterResource(id = R.drawable.ic_logo)
            } else {
                painterResource(id = R.drawable.ic_logo_dark)
            },
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier
                .padding(top = 24.dp)
                .height(56.dp)
                .constrainAs(logo) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 24.dp)
                    end.linkTo(parent.end, margin = 24.dp)
                }
        )
        Text(
            text = stringResource(id = R.string.slogan)
                .annotate(mapOf(appName to SpanStyle(fontWeight = FontWeight.Bold))),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            modifier = Modifier.constrainAs(slogan) {
                top.linkTo(logo.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(divider.top, margin = 8.dp)
            }.padding(horizontal = 24.dp)
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(vertical = 8.dp)
                .constrainAs(divider) {
                    top.linkTo(slogan.bottom, margin = 24.dp)
                    start.linkTo(slogan.start)
                    end.linkTo(slogan.end)
                    bottom.linkTo(parent.bottom, margin = 36.dp)
                }
        )
        TextButton(
            onClick = onLogin,
            modifier = Modifier.constrainAs(login) {
                top.linkTo(divider.top)
                end.linkTo(divider.start, margin = 8.dp)
                bottom.linkTo(divider.bottom)
            }
        ) {
            Text(
                text = stringResource(id = R.string.login).lowercase(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
        TextButton(
            onClick = onRegister,
            modifier = Modifier.constrainAs(register) {
                top.linkTo(divider.top)
                start.linkTo(divider.end, margin = 8.dp)
                bottom.linkTo(divider.bottom)
            }
        ) {
            Text(
                text = stringResource(id = R.string.register).lowercase(),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSetupHeader() {
    PeerTheme {
        SetupHeader(
            onLogin = {},
            onRegister = {}
        )
    }
}
