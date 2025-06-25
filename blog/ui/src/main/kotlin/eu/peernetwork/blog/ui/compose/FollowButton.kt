package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.R

/**
 * Reusable “Follow / Following” chip.
 */
@Composable
fun FollowButton(
    isFollowing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        modifier = modifier.height(32.dp),
        border   = BorderStroke(1.dp, Color.White),
        colors   = ButtonDefaults.outlinedButtonColors(
            contentColor   = Color.White,
            containerColor = Color.Transparent
        ),
        onClick = onClick
    ) {
        Text(
            if (isFollowing) stringResource(R.string.following)
            else             stringResource(R.string.follow)
        )
    }
}
