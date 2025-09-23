package eu.peernetwork.app.model

import com.google.gson.annotations.SerializedName

data class Response(
    val name: String,
    val createdAt: Long,
    val data: Map<String, Message>
) {
    data class Message(
        @SerializedName("comment") val comment: String,
        val userFriendlyComment: String
    )
}
