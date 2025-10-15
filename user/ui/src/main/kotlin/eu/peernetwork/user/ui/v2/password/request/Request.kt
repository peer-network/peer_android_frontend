package eu.peernetwork.user.ui.v2.password.request

import eu.peernetwork.core.ui.component.UiComponent

interface Request {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Request::class ],
        modules = [ RequestModule::class ]
    )
    interface Component : Request

    class Builder(private val dependency: Request) : UiComponent.DefaultBuilder<Request, Component>() {
        override fun build(context: android.content.Context): Component {
            return DaggerRequest_Component.builder()
                .request(dependency)
                .build()
        }
    }
}
