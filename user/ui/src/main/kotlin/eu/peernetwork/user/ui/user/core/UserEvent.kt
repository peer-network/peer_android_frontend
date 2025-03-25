package eu.peernetwork.user.ui.user.core

sealed interface UserEvent {
    data object Settings : UserEvent
    sealed interface Display : UserEvent {
        data object Post : Display
        data object Followers : Display
        data object Following : Display
        data object Peers : Display
    }
}
