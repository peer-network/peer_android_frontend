package eu.peernetwork.social.ui.report

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.social.ui.provider.SocialProvider

interface Report  : SocialProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [Report::class],
        modules = [ReportModule::class]
    )
    interface Component : Report {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: Report) : UiComponent.DefaultBuilder<Report, Component>() {
        override fun build(context: Context): Component {
            return DaggerReport_Component.builder().report(dependency).build()
        }
    }
}
