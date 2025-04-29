package eu.peernetwork.social.ui.member

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton

@Composable
fun FollowButton(
    userId: String,
    isInitiallyFollowing: Boolean,
    initiallyFollowedBy: Boolean,
    viewModel: MemberViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsState().value
    val context = LocalContext.current

    var isFollowing by remember(isInitiallyFollowing) { mutableStateOf(isInitiallyFollowing) }
    val isLoading = state is MemberViewModel.State.Loading

    LaunchedEffect(state) {
        when (state) {
            is MemberViewModel.State.Success -> {
                if (state.userId == userId) {
                    isFollowing = state.isFollowing
                }
            }
            is MemberViewModel.State.Error -> {
                isFollowing = !isFollowing
                Toast.makeText(context, "Error: ${state.error.message}", Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary,
        )
    )

    val (buttonText, borderColor, textColor, backgroundColor, useGradient) = when {
        isFollowing && initiallyFollowedBy -> Quintuple(
            "Peer",
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.onPrimary,
            Color.Transparent,
            true
        )
        isFollowing -> Quintuple(
            "Following",
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primary,
            Color.Transparent,
            false
        )
        else -> Quintuple(
            "Follow",
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.tertiary,
            Color.Transparent,
            false
        )
    }

    DesignOutlinedButton(
        onClick = {
            isFollowing = !isFollowing
            viewModel.follow(userId)
                  },
        modifier = modifier
            .background(
                brush = if (useGradient) gradient else Brush.linearGradient(
                    listOf(Color.Transparent, Color.Transparent)
                ),
                shape = RoundedCornerShape(50),
            ),
        enabled = !isLoading,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = Color.Transparent
        ),
        border = BorderStroke(1.dp, borderColor),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
    ) {
        Text(
            buttonText,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

data class Quintuple<T1, T2, T3, T4, T5>(val first: T1, val second: T2, val third: T3, val fourth: T4, val fifth: T5)