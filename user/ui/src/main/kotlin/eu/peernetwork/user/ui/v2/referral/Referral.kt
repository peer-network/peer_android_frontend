package eu.peernetwork.user.ui.v2.referral

import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Referral : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Referral::class ],
        modules = [ ReferralModule::class ]
    )
    interface Component : Referral {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Referral) : UiComponent.DefaultBuilder<Referral, Component>() {
        override fun build(context: android.content.Context): Component {
            return DaggerReferral_Component.builder()
                .referral(dependency)
                .build()
        }
    }
}
