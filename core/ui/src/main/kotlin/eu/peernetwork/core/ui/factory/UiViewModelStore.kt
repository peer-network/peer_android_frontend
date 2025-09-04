package eu.peernetwork.core.ui.factory

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.annotation.UiViewModel

interface UiViewModelStore {
    val state: Map<String, ViewModelStoreOwner>

    fun get(key: String): ViewModelStoreOwner

    fun remove(key: String)

    fun clear()

    class Delegate : UiViewModelStore {
        private val mutableState = mutableStateMapOf<String, ViewModelStoreOwner>()

        override val state: Map<String, ViewModelStoreOwner> get() = mutableState

        override fun get(key: String): ViewModelStoreOwner {
            return mutableState.getOrPut(key) { UiViewModel.Owner() }
        }

        override fun remove(key: String) {
            mutableState.remove(key)
        }

        override fun clear() {
            mutableState.clear()
        }
    }
}
