package eu.peernetwork.user.data.api

interface SettingsApi<T : Any> {
    interface Attribute<T : Any> : SettingsApi<T> {
        suspend operator fun invoke(value: T)
    }

    interface SecureAttribute<T : Any> : SettingsApi<T> {
        suspend operator fun invoke(value: T, password: String)
    }
}
