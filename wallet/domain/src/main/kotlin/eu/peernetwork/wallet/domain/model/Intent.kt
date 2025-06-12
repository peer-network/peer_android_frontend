package eu.peernetwork.wallet.domain.model

sealed interface Intent {
    data object Post : Intent
    data object Like : Intent
    data object DisLike : Intent
    data object Comment : Intent
}
