package eu.peernetwork.app.module.user

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import eu.peernetwork.user.remote.api.AvatarSettingsApi
import eu.peernetwork.user.remote.api.BiographySettingsApi
import eu.peernetwork.user.remote.api.EmailSettingsApi
import eu.peernetwork.user.remote.api.UsernameSettingsApi
import eu.peernetwork.user.remote.provider.SettingsProviderDelegate

@Module
internal interface SettingsModule {
    @Binds
    @IntoMap
    @Settings("username")
    fun bindUsernameSettingsApi(api: UsernameSettingsApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings("email")
    fun bindEmailSettingsApi(api: EmailSettingsApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings("bio")
    fun bindBiographySettingsApi(api: BiographySettingsApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings("avatar")
    fun bindAvatarSettingsApi(api: AvatarSettingsApi): SettingsApi<*>

    @Binds
    fun bindSettingsProvider(delegate: SettingsProviderDelegate): SettingsProvider

    @MapKey
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.BINARY)
    annotation class Settings(val value: String)
}
