package eu.peernetwork.app.ui.settings

interface SettingsEvent {
    fun invoke(event: Event)

    sealed interface Event {
        data object Tutorial : Event
    }
}
