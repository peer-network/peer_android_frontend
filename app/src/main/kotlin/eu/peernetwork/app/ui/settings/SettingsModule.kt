package eu.peernetwork.app.ui.settings

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.about.About
import eu.peernetwork.app.ui.onboarding.Onboarding
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.referral.Referral
import eu.peernetwork.user.ui.password.update.PasswordUpdate
import eu.peernetwork.user.ui.settings.account.Account
import eu.peernetwork.user.ui.settings.address.Address

@Module
object SettingsModule {
    @Provides
    @Settings.Scope
    fun provideBuilderFactory(factory: UiBuilderFactory): UiComponentProvider.Factory = factory

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Account.Builder::class)
    fun provideAccountBuilder(component: Settings.Component): UiComponent.Builder {
        return Account.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Address.Builder::class)
    fun provideAddressBuilder(component: Settings.Component): UiComponent.Builder {
        return Address.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(PasswordUpdate.Builder::class)
    fun providePasswordUpdateBuilder(component: Settings.Component): UiComponent.Builder {
        return PasswordUpdate.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(About.Builder::class)
    fun provideAboutBuilder(component: Settings.Component): UiComponent.Builder {
        return About.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Referral.Builder::class)
    fun provideReferralBuilder(component: Settings.Component): UiComponent.Builder {
        return Referral.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Profile.Builder::class)
    fun provideProfileBuilder(component: Settings.Component): UiComponent.Builder {
        return Profile.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Onboarding.Builder::class)
    fun provideOnboardingBuilder(component: Settings.Component): UiComponent.Builder {
        return Onboarding.Builder(component)
    }
}
