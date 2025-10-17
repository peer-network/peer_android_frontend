package eu.peernetwork.user.ui.v2.registration

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.FormHeader

@Composable
fun RegistrationSuccess(onLogin: () -> Unit) {
    Column {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 24.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check_rounded),
                contentDescription = stringResource(R.string.successful_message),
                modifier = Modifier.padding(vertical = 8.dp)
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally),
                tint = PeerAppGreen
            )
            FormHeader(
                title = stringResource(R.string.welcome).annotate(
                    text = stringResource(R.string.peer).lowercase(),
                    style = SpanStyle(
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold
                    )
                ),
                description = stringResource(R.string.register_message),
                horizontalAlignment = Alignment.CenterHorizontally,
                textAlign = TextAlign.Center
            )
            DesignButton(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 24.dp),
            ) { Text(stringResource(R.string.continue_text)) }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewRegistrationSuccess() {
    DesignTheme {
        RegistrationSuccess {}
    }
}
