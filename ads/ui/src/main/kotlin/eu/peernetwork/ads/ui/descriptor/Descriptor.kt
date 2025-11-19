package eu.peernetwork.ads.ui.descriptor

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Descriptor : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Descriptor::class ],
        modules = [ DescriptorModule::class ]
    )
    interface Component : Descriptor {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Descriptor) : UiComponent.DefaultBuilder<Descriptor, Component>() {
        override fun build(context: Context): Component {
            return DaggerDescriptor_Component.builder().descriptor(dependency).build()
        }
    }
}
