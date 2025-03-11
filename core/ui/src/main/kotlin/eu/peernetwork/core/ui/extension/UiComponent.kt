package eu.peernetwork.core.ui.extension

import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

fun<B : UiComponent.Builder> UiComponentProvider.builder(clazz: Class<B>): B {
    return factory().builder(clazz)
}
