package eu.peernetwork.social.ui.block

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Block: SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Block::class],
        modules = [BlockModule::class]
    )
    interface Component : Block {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Block): UiComponent.DefaultBuilder<Block, Component>() {
        override fun build(context: Context): Component {
            return DaggerBlock_Component.builder().block(dependency).build()
        }
    }
}