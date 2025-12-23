package eu.peernetwork.social.ui.referral

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.connection.Connection
import eu.peernetwork.social.ui.provider.SocialProvider

interface Referral: SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Referral::class],
        modules = [ReferralModule::class]
    )
    interface Component : Referral, Connection, UiComponentProvider {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Referral) : UiComponent.DefaultBuilder<Referral, Component>() {
        override fun build(context: Context): Component {
            return DaggerReferral_Component.builder().referral(dependency).build()
        }
    }
}
