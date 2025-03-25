package eu.peernetwork.user.ui.user.coupon

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.user.domain.provider.AccountProvider

interface UserCoupon : AccountProvider {
    @javax.inject.Scope
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Scope

    @Scope
    @dagger.Component(
        dependencies = [ UserCoupon::class ],
        modules = [ UserCouponModule::class ]
    )
    interface Component : UserCoupon {
        fun viewModelFactory(): ViewModelProvider.Factory
    }

    class Builder(private val dependency: UserCoupon) : UiComponent.DefaultBuilder<UserCoupon, Component>() {
        override fun build(context: Context): Component {
            return DaggerUserCoupon_Component.builder().userCoupon(dependency).build()
        }
    }
}
