package eu.peernetwork.user.ui.form

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier

@Composable
fun FormErrorLabel(
    error: State<String?>,
    modifier: Modifier = Modifier
) {
    AnimatedContent(targetState = error.value) { message ->
        if (message != null) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = modifier
            )
        }
    }
}
