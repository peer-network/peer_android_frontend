package eu.peernetwork.ads.ui.quote

import android.content.Context
import eu.peernetwork.ads.ui.article.Article
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider

interface Quote : AdsProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Quote::class ],
        modules = [ QuoteModule::class ]
    )
    interface Component : Quote, UiComponentProvider, Article

    class Builder(private val dependency: Quote) : UiComponent.DefaultBuilder<Quote, Component>() {
        override fun build(context: Context): Component {
            return DaggerQuote_Component.builder().quote(dependency).build()
        }
    }
}
