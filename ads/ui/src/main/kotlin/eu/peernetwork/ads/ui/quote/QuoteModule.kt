package eu.peernetwork.ads.ui.quote

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.ads.ui.article.Article
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import javax.inject.Provider

@Module
object QuoteModule {
    @Provides
    @Quote.Scope
    fun provideBuilderFactory(factory: Map<Class<out UiComponent.Builder>,
            @JvmSuppressWildcards Provider<UiComponent.Builder>>): UiComponentProvider.Factory {
        return UiBuilderFactory(factory)
    }

    @Quote.Scope
    @Provides
    @IntoMap
    @UiBuilder(Article.Builder::class)
    fun provideInvitationBuilder(component: Quote.Component): UiComponent.Builder {
        return Article.Builder(component)
    }
}
