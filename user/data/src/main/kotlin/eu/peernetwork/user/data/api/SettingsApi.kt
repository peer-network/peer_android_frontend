package eu.peernetwork.user.data.api

interface SettingsApi<T> {
    interface Updatable<T> : SettingsApi<T> {
        suspend operator fun invoke(value: T)
    }

    interface SecureUpdatable<T> : SettingsApi<T> {
        suspend operator fun invoke(value: T, password: String)
    }
}
