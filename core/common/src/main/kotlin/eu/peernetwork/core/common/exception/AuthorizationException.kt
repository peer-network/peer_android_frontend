package eu.peernetwork.core.common.exception

class AuthorizationException(val entity: String) : Throwable("The operation on entity '$entity' is unauthorized.")
