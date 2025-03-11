package eu.peernetwork.core.ui.extension

import android.app.Activity
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import kotlin.reflect.KClass

fun <T : UiComponent.Builder> Activity.findBuilder(clazz: KClass<T>): T {
    val dependency = application as UiComponent.Provider<*>
    return (dependency.injector as UiComponentProvider).factory()
        .builder(clazz.java)
}
