package eu.peernetwork.social.ui.member

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton

@Composable
fun FollowButton(
    userId: String,
    isInitiallyFollowing: Boolean,
    viewModel: MemberViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    var isFollowed by remember(isInitiallyFollowing) { mutableStateOf(isInitiallyFollowing) }
    val isLoading = state is MemberViewModel.State.Loading

    LaunchedEffect(state) {
        when (state) {
            is MemberViewModel.State.Success -> {
                if (state.userId == userId) {
                    isFollowed = state.isFollowing
                    val message = if (state.isFollowing) "Followed" else "Unfollowed"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            is MemberViewModel.State.Error -> {
                Toast.makeText(context, "Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    DesignOutlinedButton(
        onClick = {
            viewModel.follow(userId)
        },
        modifier = modifier,
        enabled = !isLoading,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(),
        border = BorderStroke(1.dp, Color.Gray),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Text(if (isFollowed) "Following" else "Follow")
        }
    }
}

