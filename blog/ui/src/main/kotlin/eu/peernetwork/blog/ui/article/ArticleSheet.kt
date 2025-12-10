package eu.peernetwork.blog.ui.article

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.core.ui.design.luna.DesignItem
import eu.peernetwork.core.ui.design.material.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.theme.DesignTheme

enum class ArticleSheetMenuItem {
    REPORT,
    SHARE,
    BOOST
}

@Composable
fun ArticleSheet(
    uuid: String,
    state: MutableState<UiPost?>,
    onMenuClicked: (ArticleSheetMenuItem, UiPost) -> Unit
) {
    val current = remember { mutableStateOf(state.value) }
    val showSheet = remember(state.value) { mutableStateOf(state.value != null) }
    val confirmed = remember { mutableStateOf<ArticleSheetMenuItem?>(null) }
    val handleConfirm by rememberUpdatedState(onMenuClicked)
    DesignBottomSheetScaffold(
        state = showSheet,
        color = MaterialTheme.colorScheme.surfaceDim,
        onDismiss = {
            state.value = null
            confirmed.value?.let {
                current.value?.let { id ->
                    handleConfirm(it, id)
                }
            }
            current.value = null
            confirmed.value = null
        }
    ) {
        ArticleSheet(
            canBoost = uuid == state.value?.author?.id
                    && state.value?.pinnedBy == null,
            onMenuClicked = {
                confirmed.value = it
                current.value = state.value
                state.value = null
            }
        ) { state.value = null }
    }
}

@Composable
fun ArticleSheet(
    onMenuClicked: (ArticleSheetMenuItem) -> Unit,
    canBoost: Boolean = false,
    onCancel: () -> Unit,
) {
    val handleMenuClicked by rememberUpdatedState(onMenuClicked)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
            .navigationBarsPadding()
    ) {
        DesignItem(
            size = 24.dp,
            label = stringResource(R.string.report_label),
            painter = painterResource(R.drawable.ic_flag),
            onClick = { handleMenuClicked(ArticleSheetMenuItem.REPORT) },
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        )
        DesignItem(
            size = 24.dp,
            label = stringResource(R.string.share_label),
            painter = painterResource(R.drawable.ic_share),
            onClick = { handleMenuClicked(ArticleSheetMenuItem.SHARE) },
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        )
        if (canBoost) {
            DesignItem(
                size = 24.dp,
                label = stringResource(R.string.boost_label),
                painter = painterResource(R.drawable.ic_boost),
                onClick = { handleMenuClicked(ArticleSheetMenuItem.BOOST) },
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            )
        }
        DesignItem(
            size = 24.dp,
            label = stringResource(R.string.cancel_label),
            painter = painterResource(R.drawable.ic_cancel),
            tint = MaterialTheme.colorScheme.error,
            color = MaterialTheme.colorScheme.error,
            onClick = onCancel,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        )
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewArticleSheet() {
    DesignTheme(isDarkMode = true) {
        ArticleSheet({}) {}
    }
}
