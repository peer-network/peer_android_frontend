package eu.peernetwork.user.ui.compose.form

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R

@Composable
fun FormHeader(
    title: AnnotatedString,
    description: String,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    FormHeader(
        title = title,
        description = description.annotate(),
        textAlign = textAlign,
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    )
}

@Composable
fun FormHeader(
    title: AnnotatedString,
    description: AnnotatedString,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    FormHeader(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment,
        title = {
            Text(
                text = title,
                textAlign = textAlign
            )
        }
    ) {
        Text(
            text = description,
            textAlign = textAlign,
        )
    }
}

@Composable
fun FormHeader(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
) {
    val updatedTitle by rememberUpdatedState(title)
    val updatedDescription by rememberUpdatedState(description)
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.surfaceContainerHigh,
            LocalTextStyle provides MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Normal
            )
        ) { updatedTitle() }
        Spacer(modifier = Modifier.height(4.dp))
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.outline,
            LocalTextStyle provides MaterialTheme.typography.bodyMedium
        ) { updatedDescription() }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFormHeader() {
    DesignTheme {
        Column(modifier = Modifier.fillMaxSize()
            .padding(24.dp)) {
            FormHeader(
                title = stringResource(R.string.login_title).annotate(),
                description = stringResource(R.string.login_description)
            )
        }
    }
}
