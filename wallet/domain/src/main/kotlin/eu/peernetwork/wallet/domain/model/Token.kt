package eu.peernetwork.wallet.domain.model

sealed interface Token {
    data object Post : Token
    data object Like : Token
    data object DisLike : Token
    data object Comment : Token
}
