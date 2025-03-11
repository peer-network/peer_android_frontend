package eu.peernetwork.core.ui.annotation

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class UiController(val key: KClass<out ViewModel>)
