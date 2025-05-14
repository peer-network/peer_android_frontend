package eu.peernetwork.core.ui.model

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.annotation.UiViewModel

@Stable
class ViewModelState {
    private val mutableState = mutableStateMapOf<String, ViewModelStoreOwner>()

    val state: Map<String, ViewModelStoreOwner> get() = mutableState

    fun get(key: String): ViewModelStoreOwner {
        return mutableState.getOrPut(key) { UiViewModel.Owner() }
    }

    fun remove(key: String) {
        mutableState.remove(key)
    }

    fun clear() {
        mutableState.clear()
    }
}
