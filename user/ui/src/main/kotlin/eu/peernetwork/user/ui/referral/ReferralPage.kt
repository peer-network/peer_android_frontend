package eu.peernetwork.user.ui.referral

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.form.FormHeader

@Composable
fun ReferralPage(
    code: TextFieldState,
    isLoading: State<Boolean>,
    error: State<String?>,
    onRequestReferral: () -> Unit,
    onVerify: (String) -> Unit
) {
    val handleOnVerify by rememberUpdatedState(onVerify)
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ) {
        FormHeader(
            title = stringResource(R.string.welcome).annotate(
                text = stringResource(R.string.peer).lowercase(),
                style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold
                )
            ),
            description = stringResource(R.string.welcome_description),
            modifier = Modifier.padding(bottom = 32.dp)
        )
        ReferralForm(
            code = code,
            isLoading = isLoading,
            error = error,
            onVerify = { handleOnVerify(code.text.toString()) },
            onRequestReferral = onRequestReferral
        )
    }
}
