package eu.peernetwork.app.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.R
import eu.peernetwork.blog.ui.compose.PostIcon
import eu.peernetwork.blog.ui.compose.PostScaffold
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun PostSnapshot(
    modifier: Modifier = Modifier,
) {
    PostScaffold(
        modifier = modifier,
        header = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                DesignAvatar {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_icon),
                        contentDescription = "profile",
                        modifier = Modifier.size(28.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(
                    text = stringResource(R.string.peernetwork_label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontStyle = FontStyle.Italic
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.peertext_label),
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.White,
                    ),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(end = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(14.dp)
                        ).padding(
                            horizontal = 12.dp,
                            vertical = 4.dp
                        )
                )
                Icon(
                    painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_menu),
                    contentDescription = "More",
                    modifier = Modifier.padding(8.dp)
                        .size(16.dp),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        toolbar = {},
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        RoundedCornerShape(24.dp)
                    )
            ) },
        footer = {},
        contentPadding = PaddingValues(12.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 4.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.onboarding_created_something_love),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = stringResource(R.string.onboarding_get_rewarded),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Normal,
                ),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PostIcon(
                    UiAction.Like,
                    "5k",
                    size = 24.dp,
                    padding = PaddingValues(0.dp)
                ) { }
                PostIcon(
                    UiAction.Dislike,
                    "5k",
                    size = 24.dp,
                    padding = PaddingValues(0.dp)
                ) { }
                PostIcon(
                    UiAction.Comment,
                    "5k",
                    size = 24.dp,
                    padding = PaddingValues(0.dp)
                ) { }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PostSnapshotPreview() {
    PeerTheme {
        PostSnapshot(Modifier.padding(16.dp))
    }
}
