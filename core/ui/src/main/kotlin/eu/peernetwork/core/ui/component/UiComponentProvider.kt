package eu.peernetwork.core.ui.component

interface UiComponentProvider {
    fun factory(): Factory

    interface Factory {
        fun <T : UiComponent.Builder> builder(clazz: Class<T>): T
    }
}
