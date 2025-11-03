package eu.peernetwork.app.ui.settings

import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import eu.peernetwork.app.ui.about.About
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.version.Version
import eu.peernetwork.core.ui.annotation.UiBuilder
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.factory.UiBuilderFactory
import eu.peernetwork.social.ui.referral.Referral
import eu.peernetwork.user.ui.password.update.Update
import eu.peernetwork.user.ui.account.Account
import eu.peernetwork.user.ui.deactivate.Deactivate
import eu.peernetwork.user.ui.email.Email
import eu.peernetwork.user.ui.logout.Logout

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
    @UiBuilder(Email.Builder::class)
    fun provideAddressBuilder(component: Settings.Component): UiComponent.Builder {
        return Email.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Update.Builder::class)
    fun providePasswordUpdateBuilder(component: Settings.Component): UiComponent.Builder {
        return Update.Builder(component)
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
    @UiBuilder(Version.Builder::class)
    fun provideVersionBuilder(component: Settings.Component): UiComponent.Builder {
        return Version.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Logout.Builder::class)
    fun provideLogoutBuilder(component: Settings.Component): UiComponent.Builder {
        return Logout.Builder(component)
    }

    @Settings.Scope
    @Provides
    @IntoMap
    @UiBuilder(Deactivate.Builder::class)
    fun provideDeactivateBuilder(component: Settings.Component): UiComponent.Builder {
        return Deactivate.Builder(component)
    }
}
