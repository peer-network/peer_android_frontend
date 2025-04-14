package eu.peernetwork.blog.ui.comment

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.blog.ui.provider.BlogProvider
import eu.peernetwork.core.ui.component.UiComponent

interface Comment : BlogProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Comment::class],
        modules = [CommentModule::class]
    )
    interface Component : Comment {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Comment) : UiComponent.DefaultBuilder<Comment, Component>() {
        override fun build(context: Context): Component {
            return DaggerComment_Component.builder().comment(dependency).build()
        }
    }
}