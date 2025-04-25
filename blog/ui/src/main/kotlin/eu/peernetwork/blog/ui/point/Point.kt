package eu.peernetwork.blog.ui.point

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Point : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ Point::class ],
        modules = [ PointModule::class ]
    )
    interface Component : Point {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Point) : UiComponent.DefaultBuilder<Point, Component>() {
        override fun build(context: Context): Component {
            return DaggerPoint_Component.builder().point(dependency).build()
        }
    }
}
