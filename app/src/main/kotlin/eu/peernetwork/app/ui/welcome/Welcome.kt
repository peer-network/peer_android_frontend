package eu.peernetwork.app.ui.welcome

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.user.ui.v2.login.Login
import eu.peernetwork.user.ui.v2.password.request.Request
import eu.peernetwork.user.ui.v2.password.reset.Reset
import eu.peernetwork.user.ui.v2.password.verification.Verification
import eu.peernetwork.user.ui.v2.referral.Referral
import eu.peernetwork.user.ui.v2.registration.Registration

interface Welcome : ApplicationProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Welcome::class ],
        modules = [ WelcomeModule::class ]
    )
    interface Component : Welcome,
        UiComponentProvider,
        Login,
        Referral,
        Registration,
        Request,
        Reset,
        Verification

    class Builder(private val dependency: Welcome) : UiComponent.DefaultBuilder<Welcome, Component>() {
        override fun build(context: Context): Component {
            return DaggerWelcome_Component.builder().welcome(dependency).build()
        }
    }
}
