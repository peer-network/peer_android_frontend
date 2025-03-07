package eu.peernetwork.core.common.exception

open class BusinessException(val label: String, cause: String) : Throwable(cause)
