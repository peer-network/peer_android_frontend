package eu.peernetwork.core.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.exception.NoContentException

@Composable
fun ResourceInteractor.error(error: Throwable): String {
    val errorMessage = stringResource(R.string.unknown_error_message)
    if (error is NoContentException) {
        return stringResource(R.string.empty_message)
    }
    return error.message?.let { key ->
        string(key)
    } ?: errorMessage
}
