package eu.peernetwork.user.data.api

interface SettingsApi<T : Any> {
    interface Updatable<T : Any> : SettingsApi<T> {
        suspend operator fun invoke(value: T)
    }

    interface SecureUpdatable<T : Any> : SettingsApi<T> {
        suspend operator fun invoke(value: T, password: String)
    }
}
