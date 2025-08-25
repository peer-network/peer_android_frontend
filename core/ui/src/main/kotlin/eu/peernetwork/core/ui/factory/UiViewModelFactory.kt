package eu.peernetwork.core.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.exception.UiViewModelException
import javax.inject.Provider

class UiViewModelFactory(
    private val classToViewModel:
    @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return classToViewModel[modelClass]?.get() as? T
            ?: throw UiViewModelException(modelClass.name)
    }
}
