package eu.peernetwork.core.ui.factory

import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.exception.UiBuilderException
import javax.inject.Inject

class UiBuilderFactory @Inject constructor(
    private val factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards javax.inject.Provider<UiComponent.Builder>>
) : UiComponentProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : UiComponent.Builder> builder(clazz: Class<T>): T {
        return (factory[clazz]?.get() as? T?) ?: throw UiBuilderException(clazz.name)
    }
}
