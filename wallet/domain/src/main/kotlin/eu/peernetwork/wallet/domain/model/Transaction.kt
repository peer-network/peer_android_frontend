package eu.peernetwork.wallet.domain.model

data class Transaction(
    val id: String,
    val sender: User,
    val recipient: User,
    val message: String,
    val category: Category,
    val amount: Amount,
    val fee: Fee,
) {
    enum class Type {
        TRANSACTION,
        AIRDROP,
        PAYMENT,
        BURN,
        MINT
    }
}
