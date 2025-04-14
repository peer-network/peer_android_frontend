package eu.peernetwork.blog.ui.point

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface BlogPoint : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ BlogPoint::class ],
        modules = [ BlogPointModule::class ]
    )
    interface Component : BlogPoint {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: BlogPoint) : UiComponent.DefaultBuilder<BlogPoint, Component>() {
        override fun build(context: Context): Component {
            return DaggerBlogPoint_Component.builder().blogPoint(dependency).build()
        }
    }
}
