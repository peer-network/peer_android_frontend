package eu.peernetwork.app.ui.wallet

import android.content.Context
import eu.peernetwork.app.provider.ApplicationProvider
import eu.peernetwork.app.ui.profile.Profile
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.social.ui.search.member.Member
import eu.peernetwork.wallet.ui.overview.Overview
import eu.peernetwork.wallet.ui.service.Service
import eu.peernetwork.wallet.ui.transfer.Transfer

interface Wallet : ApplicationProvider {
    fun settingsEvent(): SettingsEvent

    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Wallet::class ],
        modules = [ WalletModule::class ]
    )
    interface Component : Wallet, Overview, Transfer, Member, Profile, Service, UiComponentProvider

    class Builder(private val dependency: Wallet): UiComponent.DefaultBuilder<Wallet, Component>() {
        override fun build(context: Context): Component {
            return DaggerWallet_Component.builder().wallet(dependency).build()
        }
    }
}
