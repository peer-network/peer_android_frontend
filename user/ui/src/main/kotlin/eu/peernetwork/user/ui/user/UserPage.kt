package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignAnnotatedText
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.core.ui.design.luna.DesignAvatar
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiMetric

@Composable
fun UserPage(
    account: UiAccount,
    isAdmin: Boolean,
    modifier: Modifier = Modifier,
    onClick: (UserMetric) -> Unit,
    content: @Composable () -> Unit,
) {
    val handleOnClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(content)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val emptyDescription = stringResource(R.string.empty_description_message)
    Column {
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DesignAvatar {
                    DesignImage(
                        label = account.username,
                        imageUrl = account.imageUrl,
                        size = 56.dp,
                        color = MaterialTheme.colorScheme.surfaceContainerLow,
                        modifier = Modifier.clickable(
                            role = Role.Button,
                            enabled = true
                        ) { selectedImage.value = account.imageUrl }
                    )
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "${account.username} #${account.slug}"
                            .annotate(
                                text = "#${account.slug}",
                                style = SpanStyle(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                        ),
                        color = MaterialTheme.colorScheme.onBackground,
                        overflow = TextOverflow.Ellipsis,
                    )
                    UserMetric(
                        overview = account.metric,
                        labelColor = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp),
                        onClick = {
                            if (!(!isAdmin && it == UserMetric.PEER)) {
                                handleOnClick(it)
                            }
                        }
                    )
                }
            }
            DesignAnnotatedText(
                text = (account.bio ?: emptyDescription).annotate(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 14.dp)
            )
        }
        updatedContent()
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
fun PreviewUserPage() {
    DesignTheme(isDarkMode = false) {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            metric = UiMetric(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isFollowing = false,
            isFollowed = false
        )
        UserPage(
            account = model,
            isAdmin = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(vertical = 16.dp),
            onClick = {}
        ) {}
    }
}
