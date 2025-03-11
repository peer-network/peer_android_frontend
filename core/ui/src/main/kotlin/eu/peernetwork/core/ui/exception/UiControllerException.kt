package eu.peernetwork.core.ui.exception

class UiControllerException(name: String) : Throwable("No ViewModel mapping found for class: $name")
