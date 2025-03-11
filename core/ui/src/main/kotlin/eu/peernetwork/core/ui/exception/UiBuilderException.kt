package eu.peernetwork.core.ui.exception

class UiBuilderException(name: String) : Throwable("Builder $name not found! Is this fragment a child fragment of the page?")
