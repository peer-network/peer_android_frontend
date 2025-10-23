package eu.peernetwork.user.ui.user

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.luna.DesignButton
import eu.peernetwork.core.ui.design.luna.designSecondaryButtonColors
import eu.peernetwork.core.ui.design.luna.designTertiaryButtonColors
import eu.peernetwork.core.ui.design.material.DesignAsyncImage
import eu.peernetwork.core.ui.design.material.DesignAvatar
import eu.peernetwork.core.ui.design.material.DesignLead
import eu.peernetwork.core.ui.design.material.DesignOverlay
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.toInt
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.compose.account.Metrics
import eu.peernetwork.user.ui.compose.account.ProfileScaffold
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiOverview

@Composable
fun UserPage(
    account: UiAccount,
    modifier: Modifier = Modifier,
    showPeers: Boolean,
    avatar: @Composable () -> Unit = {},
    connection: @Composable (Pair<Boolean, Boolean>) -> Unit,
    onSettings: (() -> Unit)? = null,
    onClick: (Int) -> Unit,
) {
    val clickHandler by rememberUpdatedState(onClick)
    val settingsHandler by rememberUpdatedState(onSettings)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val updatedAvatar by rememberUpdatedState(avatar)
    val updatedConnection by rememberUpdatedState(connection)
    val emptyDescription = stringResource(R.string.empty_description_message)
    val visible = remember(selectedImage.value) { mutableStateOf(selectedImage.value != null) }
    ProfileScaffold(
        modifier = modifier,
        avatar = {
            DesignAsyncImage(
                account.username,
                account.imageUrl,
                modifier = Modifier.clickable(role = Role.Button, enabled = true) {
                    selectedImage.value = account.imageUrl
                }) },
        actions = {
            if (settingsHandler != null) {
                IconButton(onClick = { settingsHandler?.invoke() }) {
                    Icon(
                        painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_settings),
                        contentDescription = stringResource(eu.peernetwork.core.ui.R.string.settings_label),
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(bottom = 4.dp)
                ) { updatedConnection(account.isfollowing to account.isfollowed) }
            }
        },
        options = {
            Metrics(
                overview = account.overview,
                modifier = Modifier.fillMaxWidth(),
                onClick = { if (it < (2 + showPeers.toInt())) clickHandler(it) }
            )
        }
    ) {
        DesignLead(
            account.username,
            account.slug.toString(),
            account.bio ?: emptyDescription
        )
    }
    DesignOverlay(
        visible,
        onDismiss = { selectedImage.value = null }
    ) { updatedAvatar() }
}

@Composable
fun UserPage(
    account: UiAccount,
    showPeers: Boolean,
    onClick: (Int) -> Unit,
) {
    val clickHandler by rememberUpdatedState(onClick)
    val selectedImage = remember { mutableStateOf<String?>(null) }
    val emptyDescription = stringResource(R.string.empty_description_message)
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            DesignAvatar {
                DesignAsyncImage(
                    account.username,
                    account.imageUrl,
                    modifier = Modifier.clickable(role = Role.Button, enabled = true) {
                        selectedImage.value = account.imageUrl
                    }
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
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    overflow = TextOverflow.Ellipsis,
                )
                Metrics(
                    overview = account.overview,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 4.dp),
                    onClick = { if (it < (2 + showPeers.toInt())) clickHandler(it) }
                )
            }
        }
        Text(
            text = account.bio ?: emptyDescription,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.tertiary
            ),
            modifier = Modifier.padding(vertical = 12.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DesignButton(
                onClick = {},
                minHeight = 42.dp,
                colors = designSecondaryButtonColors(),
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Invite a friend") }
            DesignButton(
                onClick = {},
                minHeight = 42.dp,
                contentPadding = PaddingValues(horizontal = 16.dp),
                colors = designTertiaryButtonColors(),
                modifier = Modifier.weight(1f)
            ) { Text("Settings") }
            IconButton({}) {
                Icon(
                    painter = painterResource(eu.peernetwork.core.ui.R.drawable.ic_menu),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserPage() {
    DesignTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            overview = UiOverview(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isfollowing = false,
            isfollowed = false
        )
        Column(modifier = Modifier.fillMaxWidth()
            .padding(24.dp)) {
            UserPage(model, showPeers = true) {}
            // UserPage(connection = { }, account = model, showPeers = true) {}
        }
    }
}
