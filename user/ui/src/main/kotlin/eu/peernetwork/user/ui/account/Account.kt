package eu.peernetwork.user.ui.account

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Account : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Account::class ],
        modules = [ AccountModule::class ]
    )
    interface Component : Account {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Account) : UiComponent.DefaultBuilder<Account, Component>() {
        override fun build(context: Context): Component {
            return DaggerAccount_Component.builder().account(dependency).build()
        }
    }
}
