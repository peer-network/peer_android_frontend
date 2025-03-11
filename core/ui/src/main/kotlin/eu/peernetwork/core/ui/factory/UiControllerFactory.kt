package eu.peernetwork.core.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.exception.UiControllerException
import javax.inject.Inject
import javax.inject.Provider

class UiControllerFactory  @Inject constructor(
    private val classToViewModel:
    @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return classToViewModel[modelClass]?.get() as? T
            ?: throw UiControllerException(modelClass.name)
    }
}
