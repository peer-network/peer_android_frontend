package eu.peernetwork.app.ui.wallet

import android.content.Context
import eu.peernetwork.core.ui.component.UiComponent

interface Wallet {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Wallet::class ],
    )
    interface Component : Wallet

    class Builder(private val dependency: Wallet) : UiComponent.DefaultBuilder<Wallet, Component>() {
        override fun build(context: Context): Component {
            return DaggerWallet_Component.builder().wallet(dependency).build()
        }
    }
}
