package eu.peernetwork.blog.ui.event

interface UiPostEvent {
    fun onMentionClick(username: String)
    fun onHashtagClick(tag: String)
    fun onPostClick(id: String, position: Int)
    fun onMediaClick(id: String, position: Int)
    fun onAuthorClick(id: String)
}
