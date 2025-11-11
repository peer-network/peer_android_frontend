package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiFilter
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Sort
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignDropDown
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost

@Composable
fun FeedMenu(
    id: String,
    default: Int,
    title: String? = null,
    onSelect: (Int, Criteria) -> Unit,
    onHome: () -> Unit
) {
    val handleOnSelect by rememberUpdatedState(onSelect)
    val relations = mapOf(
        UiFilter.NONE to Criteria.Content(Sort.NEW),
        UiFilter.TRENDS to Criteria.Content(Sort.TREND),
        UiFilter.MOST_LIKED to Criteria.Content(Sort.MOST_LIKED),
        UiFilter.MOST_VIEWED to Criteria.Content(Sort.MOST_VIEWED),
        UiFilter.MOST_DISLIKED to Criteria.Content(Sort.MOST_DISLIKED)
    )
    DesignTitleBarHost(
        tag = "FeedScreen$id$title",
        listener = onHome
    ) {
        titleBar {
            val expanded = remember { mutableStateOf(false) }
            if (title != null) {
                DesignTitle { Text(title) }
            } else {
                DesignDropDown(
                    expanded,
                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                    contentPadding = PaddingValues(vertical = 4.dp),
                    default = (UiFilter.entries.getOrNull(default) ?: UiFilter.NONE).name,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                ) {
                    relations.entries.forEach {
                        item(tag = it.key.name, {
                            handleOnSelect(it.key.ordinal, it.value)
                            true
                        }) { label, isActive ->
                            val filter = UiFilter.valueOf(label)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 8.dp)
                                    .padding(vertical = 1.dp),
                            ) {
                                Text(
                                    stringResource(filter.value),
                                    style = if (isActive) {
                                        MaterialTheme.typography.bodyMedium.copy(
                                            MaterialTheme.colorScheme.onBackground
                                        )
                                    } else {
                                        MaterialTheme.typography.bodyMedium.copy(
                                            MaterialTheme.colorScheme.tertiary
                                        )
                                    },
                                )
                                if (isActive) {
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Icon(
                                        painter = painterResource(R.drawable.ic_caret_down),
                                        contentDescription = stringResource(filter.value),
                                        modifier = Modifier.size(16.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                } else {
                                    Spacer(modifier = Modifier.width(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
