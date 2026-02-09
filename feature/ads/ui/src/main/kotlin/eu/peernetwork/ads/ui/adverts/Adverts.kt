package eu.peernetwork.ads.ui.adverts

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Adverts : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Adverts::class ],
        modules = [ AdvertsModule::class ]
    )
    interface Component : Adverts {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Adverts) : UiComponent.DefaultBuilder<Adverts, Component>() {
        override fun build(context: Context): Component {
            return DaggerAdverts_Component.builder().adverts(dependency).build()
        }
    }
}
