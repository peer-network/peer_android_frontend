package eu.peernetwork.user.domain.model

data class User(
    val id: String,
    val slug: Int,
    val username: String,
    val bio: String,
    val imageUrl: String,
    val isAccessible: Boolean,
    val status: Status
) {
    data class Profile(
        val id: String,
        val slug: String,
        val username: String,
        val imageUrl: String
    )
}
