package eu.peernetwork.app.ui.composer

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.blog.ui.creator.Creator
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.media.ui.attachment.Attachment
import eu.peernetwork.media.ui.selector.explorer.Explorer
import eu.peernetwork.wallet.ui.confirmation.Confirmation

interface Composer : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Composer::class ],
        modules = [ ComposerModule::class ]
    )
    interface Component : Composer,
        Creator,
        Explorer,
        Attachment,
        Confirmation,
        UiComponentProvider

    class Builder(private val dependency: Composer) : UiComponent.DefaultBuilder<Composer, Component>() {
        override fun build(context: Context): Component {
            return DaggerComposer_Component.builder().composer(dependency).build()
        }
    }
}
