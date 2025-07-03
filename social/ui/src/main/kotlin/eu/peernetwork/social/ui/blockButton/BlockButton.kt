package eu.peernetwork.social.ui.blockButton

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface BlockButton: SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [BlockButton::class],
        modules = [BlockButtonModule::class]
    )
    interface Component : BlockButton {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency:BlockButton): UiComponent.DefaultBuilder<BlockButton, Component>() {
        override fun build(context: Context): Component {
            return DaggerBlockButton_Component.builder().blockButton(dependency).build()
        }
    }

}