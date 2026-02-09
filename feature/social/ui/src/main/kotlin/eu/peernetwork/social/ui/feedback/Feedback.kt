package eu.peernetwork.social.ui.feedback

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.persistence.domain.observable.ObservableLong
import eu.peernetwork.persistence.domain.publishable.PublishableLong
import eu.peernetwork.persistence.domain.retrievable.RetrievableLong
import eu.peernetwork.social.ui.provider.SocialProvider

interface Feedback : SocialProvider {
    fun retrievableLong(): RetrievableLong

    fun publishableLong(): PublishableLong

    fun observableLong(): ObservableLong

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Feedback::class ],
        modules = [ FeedbackModule::class ]
    )
    interface Component : Feedback {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Feedback) : UiComponent.DefaultBuilder<Feedback, Component>() {
        override fun build(context: Context): Component {
            return DaggerFeedback_Component.builder().feedback(dependency).build()
        }
    }
}