package eu.peernetwork.wallet.ui.model

import androidx.compose.ui.text.AnnotatedString

data class UiTransaction(
    val id: String,
    val res: Int?,
    val icon: Int?,
    val sender: UiUser,
    val recipient: UiUser,
    val category: UiCategory,
    val amount: UiAmount,
    val fees: UiFees?,
    val message: AnnotatedString?,
    val createdAt: String,
)
