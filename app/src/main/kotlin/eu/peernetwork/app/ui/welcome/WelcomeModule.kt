package eu.peernetwork.app.ui.welcome

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.user.ui.login.Login
import eu.peernetwork.user.ui.password.request.Request
import eu.peernetwork.user.ui.password.reset.Reset
import eu.peernetwork.user.ui.password.verification.Verification
import eu.peernetwork.user.ui.referral.Referral
import eu.peernetwork.user.ui.registration.Registration

@Module
object WelcomeModule {
    @Provides
    @Welcome.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Login.Builder::class)
    fun provideLoginBuilder(component: Welcome.Component): UiComponent.Builder {
        return Login.Builder(component)
    }

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Referral.Builder::class)
    fun provideReferralBuilder(component: Welcome.Component): UiComponent.Builder {
        return Referral.Builder(component)
    }

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Registration.Builder::class)
    fun provideRegistrationBuilder(component: Welcome.Component): UiComponent.Builder {
        return Registration.Builder(component)
    }

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Request.Builder::class)
    fun provideRequestBuilder(component: Welcome.Component): UiComponent.Builder {
        return Request.Builder(component)
    }

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Reset.Builder::class)
    fun provideResetBuilder(component: Welcome.Component): UiComponent.Builder {
        return Reset.Builder(component)
    }

    @Provides
    @IntoMap
    @Welcome.Scope
    @UiBuilder(Verification.Builder::class)
    fun provideVerificationBuilder(component: Welcome.Component): UiComponent.Builder {
        return Verification.Builder(component)
    }
}
