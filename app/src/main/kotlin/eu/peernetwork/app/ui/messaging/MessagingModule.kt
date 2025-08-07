package eu.peernetwork.app.ui.messaging

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.messaging.ui.chat.Chat

@Module
object MessagingModule {
    @Provides
    @Messaging.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Messaging.Scope
    @Provides
    @IntoMap
    @UiBuilder(Chat.Builder::class)
    fun provideChatBuilder(component: Messaging.Component): UiComponent.Builder {
        return Chat.Builder(component)
    }
}