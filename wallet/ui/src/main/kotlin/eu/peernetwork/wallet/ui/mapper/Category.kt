package eu.peernetwork.wallet.ui.mapper

import androidx.annotation.StringRes
import eu.peernetwork.wallet.domain.model.Category
import eu.peernetwork.wallet.ui.R
import eu.peernetwork.wallet.ui.model.UiCategory

fun Category.mapFromDomain(): UiCategory {
    return when (this) {
        Category.AD -> UiCategory.AD
        Category.MINT -> UiCategory.MINT
        Category.POST -> UiCategory.POST
        Category.PURCHASE -> UiCategory.PURCHASE
        Category.TRANSFER -> UiCategory.TRANSFER
        Category.COMMENT -> UiCategory.COMMENT
        Category.LIKE -> UiCategory.LIKE
        Category.DISLIKE -> UiCategory.DISLIKE
        Category.DEFAULT -> UiCategory.DEFAULT
    }
}

@StringRes
fun Category.toResource(): Int {
    return when (this) {
        Category.AD -> R.string.pinned_post_transaction_label
        Category.MINT -> R.string.mint_transaction_label
        Category.POST -> R.string.post_transaction_label
        Category.PURCHASE -> R.string.purchase_transaction_label
        Category.TRANSFER -> R.string.user_transaction_label
        Category.COMMENT -> R.string.comment_transaction_label
        Category.LIKE -> R.string.like_transaction_label
        Category.DISLIKE -> R.string.dislike_transaction_label
        Category.DEFAULT -> R.string.default_transaction_label
    }
}

@StringRes
fun Category.toIcon(): Int {
    return when (this) {
        Category.AD -> R.drawable.ic_love
        Category.MINT -> R.drawable.ic_love
        Category.POST -> R.drawable.ic_love
        Category.PURCHASE -> R.drawable.ic_love
        Category.TRANSFER -> R.drawable.ic_love
        Category.COMMENT -> R.drawable.ic_love
        Category.LIKE -> R.drawable.ic_love
        Category.DISLIKE -> R.drawable.ic_hate
        Category.DEFAULT -> R.drawable.ic_love
    }
}
