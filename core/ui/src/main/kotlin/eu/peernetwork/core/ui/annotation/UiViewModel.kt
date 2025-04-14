package eu.peernetwork.core.ui.annotation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class UiViewModel(val key: KClass<out ViewModel>) {
    class Owner : ViewModelStoreOwner {
        private val store = ViewModelStore()

        override val viewModelStore: ViewModelStore get() = store
    }
}
