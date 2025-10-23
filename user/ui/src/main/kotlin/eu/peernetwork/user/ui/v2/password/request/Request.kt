package eu.peernetwork.user.ui.v2.password.request

import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.ui.provider.UserProvider

interface Request : UserProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Request::class ],
        modules = [ RequestModule::class ]
    )
    interface Component : Request {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Request) : UiComponent.DefaultBuilder<Request, Component>() {
        override fun build(context: android.content.Context): Component {
            return DaggerRequest_Component.builder()
                .request(dependency)
                .build()
        }
    }
}
