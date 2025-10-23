package eu.peernetwork.user.ui.referral

import android.content.res.Configuration
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.DesignTextField
import eu.peernetwork.core.ui.extension.isValidInput
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.form.ErrorLabel
import java.util.UUID

private const val tag = "PEER_REFERRAL"

@Composable
fun ReferralForm(
    code: TextFieldState,
    isLoading: State<Boolean>,
    error: State<String?>,
    onVerify: (String) -> Unit,
    onRequestReferral: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val handleOnVerify by rememberUpdatedState(onVerify)
    val handleOnRequestReferral by rememberUpdatedState(onRequestReferral)
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.referral_request))
        append(" ")
        pushStringAnnotation(tag = tag, annotation = tag)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Medium
            )
        ) { append(stringResource(R.string.click_here)) }
        append(" ")
        append(stringResource(R.string.referral_request_intent))
        pop()
    }
    val isValidate = remember(code.text) { derivedStateOf { code.isValidInput() } }
    var layoutResult: TextLayoutResult? = null
    Column(modifier = modifier) {
        DesignTextField(
            state = code,
            enabled = !isLoading.value,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            lineLimits = TextFieldLineLimits.SingleLine,
            hint = stringResource(id = R.string.referral_code),
            leading = {
                Icon(
                    painter = painterResource(R.drawable.ic_referral),
                    contentDescription = stringResource(id = R.string.referral_code),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(22.dp),
                    tint = LocalContentColor.current
                )
            },
            modifier = Modifier.padding(bottom = 6.dp)
        )
        ErrorLabel(
            error = error,
            modifier = Modifier.padding(horizontal = 18.dp)
                .padding(bottom = 4.dp)
        )
        DesignButton(
            onClick = { handleOnVerify(code.text.toString()) },
            enabled = !isLoading.value && isValidate.value,
            isLoading = isLoading.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
        ) { Text(stringResource(R.string.referral_verification)) }
        Text(
            text = annotatedString,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.scrim,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .align(Alignment.CenterHorizontally)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        layoutResult?.let { layout ->
                            val position = layout.getOffsetForPosition(offset)
                            annotatedString.getStringAnnotations(
                                tag = tag,
                                start = position,
                                end = position
                            ).firstOrNull()?.let { _ ->
                                handleOnRequestReferral()
                            }
                        }
                    }
                },
            onTextLayout = { layoutResult = it }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewReferralForm() {
    DesignTheme {
        val code = remember { TextFieldState(UUID.randomUUID().toString()) }
        val isLoading = remember { mutableStateOf(false) }
        val error = remember { mutableStateOf(null) }
        ReferralForm(
            code = code,
            isLoading = isLoading,
            error = error,
            modifier = Modifier.padding(24.dp),
            onVerify = {},
            onRequestReferral = {},
        )
    }
}
