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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.extension.builder

data class Quintuple<T1, T2, T3, T4, T5>(
    val first: T1,
    val second: T2,
    val third: T3,
    val fourth: T4,
    val fifth: T5
)

@Composable
fun FollowButton(
    viewModelStoreOwner: ViewModelStoreOwner,
    provider: UiComponentProvider,
    content: @Composable ((String) -> Unit, Throwable?, MemberViewModel.State.Content?) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val viewModel: MemberViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state = viewModel.state.collectAsState().value

    content(
        { viewModel.follow(it) },
        (state as? MemberViewModel.State.Error)?.error,
        state as? MemberViewModel.State.Content
    )
}

@Composable
fun FollowButtonStateless(
    isFollowing: Boolean,
    error: Throwable?,
    initiallyFollowedBy: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
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
        onClick = onClick,
        modifier = modifier.background(
            brush = if (useGradient) gradient else Brush.linearGradient(
                listOf(Color.Transparent, Color.Transparent)
            ),
            shape = RoundedCornerShape(50),
        ),
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
