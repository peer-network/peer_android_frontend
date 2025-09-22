package eu.peernetwork.blog.ui.interaction.listing

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Listing : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Listing::class],
        modules = [ListingModule::class]
    )
    interface Component : Listing {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Listing) : UiComponent.DefaultBuilder<Listing, Component>() {
        override fun build(context: Context): Component {
            return DaggerListing_Component.builder().listing(dependency).build()
        }
    }
}
