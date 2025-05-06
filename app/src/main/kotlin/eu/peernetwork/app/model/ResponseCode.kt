package eu.peernetwork.app.model

data class ResponseCode(
    val name: String,
    val createdAt: Long,
    val data: Map<String, Message>
) {
    data class Message(
        val comment: String,
        val userFriendlyComment: String
    )
}
