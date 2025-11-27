package eu.peernetwork.blog.ui.model.v2

sealed interface UiTimer {
    data object Now : UiTimer
    data class Minutes(val value: Long) : UiTimer
    data class Hours(val value: Long) : UiTimer
    data class Days(val value: Long) : UiTimer
    data object Yesterday : UiTimer
    data class Date(val value: String) : UiTimer
}
