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
import eu.peernetwork.app.mapper.mapFromDomain
import eu.peernetwork.app.model.UiRelation
import eu.peernetwork.blog.domain.model.Relation
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignDropDown
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost

@Composable
fun FeedMenu(
    id: String,
    title: String? = null,
    relation: Relation,
    onSelect: (Relation) -> Unit,
    onHome: () -> Unit
) {
    val handleOnSelect by rememberUpdatedState(onSelect)
    val relations = mapOf(
        stringResource(UiRelation.ALL.value) to Relation.NONE,
        stringResource(UiRelation.FOLLOWER.value) to Relation.FOLLOWER,
        stringResource(UiRelation.FOLLOWED.value) to Relation.FOLLOWED,
    )
    DesignTitleBarHost(
        "FeedScreen$id$title",
        onHome) {
        titleBar {
            var expanded = remember { mutableStateOf(false) }
            if (title != null) {
                DesignTitle { Text(title) }
            } else {
                DesignDropDown(
                    expanded,
                    contentPadding = PaddingValues(vertical = 4.dp),
                    default = stringResource(relation.mapFromDomain().value),
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    relations.entries.forEach {
                        item(tag = it.key, {
                            handleOnSelect(it.value)
                            true
                        }) { label, isActive ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(start = 8.dp)
                                    .padding(vertical = 2.dp),
                            ) {
                                Text(
                                    label,
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
                                        contentDescription = label,
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
