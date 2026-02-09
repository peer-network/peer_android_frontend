package eu.peernetwork.wallet.ui.reward

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface Reward : WalletProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Reward::class ],
        modules = [ RewardModule::class ]
    )
    interface Component : Reward {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Reward) : UiComponent.DefaultBuilder<Reward, Component>() {
        override fun build(context: Context): Component {
            return DaggerReward_Component.builder().reward(dependency).build()
        }
    }
}
