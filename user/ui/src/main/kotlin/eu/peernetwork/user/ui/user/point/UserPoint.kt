package eu.peernetwork.user.ui.user.point

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.domain.provider.AccountProvider

interface UserPoint : AccountProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ UserPoint::class ],
        modules = [ UserPointModule::class ]
    )
    interface Component : UserPoint {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: UserPoint) : UiComponent.DefaultBuilder<UserPoint, Component>() {
        override fun build(context: Context): Component {
            return DaggerUserPoint_Component.builder().userPoint(dependency).build()
        }
    }
}
