package eu.peernetwork.blog.ui.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.exception.NoContentException

@Composable
fun TimelineError(
    error: State<Throwable?>,
    component: Timeline.Component,
    onRefresh: () -> Unit,
    onExplore: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (error.value is NoContentException) {
            TimelineEmpty(onClick = onExplore)
        } else {
            error.value?.message?.let {
                TimelineError(
                    error = component.resource().string(it),
                    onRefresh = onRefresh
                )
            }
        }
    }
}

@Composable
fun TimelineError(
    error: String,
    onRefresh: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 72.dp)
    ) {
        Text(
            text = error,
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp)
        )
        DesignButton(
            onClick = onRefresh,
            minHeight = 42.dp,
            contentPadding = PaddingValues(
                vertical = 12.dp,
                horizontal = 36.dp
            ),
            colors = designSecondaryButtonColors(),
            modifier = Modifier.padding(top = 10.dp),
        ) { Text(stringResource(R.string.retry_label)) }
    }
}
