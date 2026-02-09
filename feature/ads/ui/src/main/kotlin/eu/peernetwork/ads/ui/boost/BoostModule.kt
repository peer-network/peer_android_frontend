package eu.peernetwork.ads.ui.boost

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.checkout.Checkout
import eu.peernetwork.ads.ui.descriptor.Descriptor
import eu.peernetwork.ads.ui.quote.Quote
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import javax.inject.Provider

@Module
object BoostModule {
    @Provides
    @Boost.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Boost.Scope
    @Provides
    @IntoMap
    @UiBuilder(Quote.Builder::class)
    fun provideQuoteBuilder(component: Boost.Component): UiComponent.Builder {
        return Quote.Builder(component)
    }

    @Boost.Scope
    @Provides
    @IntoMap
    @UiBuilder(Checkout.Builder::class)
    fun provideCheckoutBuilder(component: Boost.Component): UiComponent.Builder {
        return Checkout.Builder(component)
    }

    @Boost.Scope
    @Provides
    @IntoMap
    @UiBuilder(Descriptor.Builder::class)
    fun provideDescriptorBuilder(component: Boost.Component): UiComponent.Builder {
        return Descriptor.Builder(component)
    }
}
