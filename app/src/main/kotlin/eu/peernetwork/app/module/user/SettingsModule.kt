package eu.peernetwork.app.module.user

import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import eu.peernetwork.app.api.AvatarApi
import eu.peernetwork.app.api.BiographyApi
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.data.provider.SettingsProvider
import eu.peernetwork.user.remote.api.EmailSettingsApi
import eu.peernetwork.user.remote.api.UsernameSettingsApi
import eu.peernetwork.user.remote.provider.SettingsProviderDelegate
import eu.peernetwork.user.ui.model.UiSettings

@Module
internal interface SettingsModule {
    @Binds
    @IntoMap
    @Settings(UiSettings.USERNAME)
    fun bindUsernameSettingsApi(api: UsernameSettingsApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings(UiSettings.EMAIL)
    fun bindEmailSettingsApi(api: EmailSettingsApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings(UiSettings.BIO)
    fun bindBiographySettingsApi(api: BiographyApi): SettingsApi<*>

    @Binds
    @IntoMap
    @Settings(UiSettings.AVATAR)
    fun bindBiographyApi(api: AvatarApi): SettingsApi<*>

    @Binds
    fun bindSettingsProvider(delegate: SettingsProviderDelegate): SettingsProvider

    @MapKey
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.BINARY)
    annotation class Settings(val value: String)
}
