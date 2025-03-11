package eu.peernetwork.core.ui.annotation

import dagger.MapKey
import eu.peernetwork.core.ui.component.UiComponent
import kotlin.reflect.KClass

@MapKey
annotation class UiBuilder(val key: KClass<out UiComponent.Builder>)
