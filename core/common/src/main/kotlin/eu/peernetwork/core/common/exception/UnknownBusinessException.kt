package eu.peernetwork.core.common.exception

class UnknownBusinessException(label: String, val status: String) : BusinessException(label, "Unknown business exception occurred!")
