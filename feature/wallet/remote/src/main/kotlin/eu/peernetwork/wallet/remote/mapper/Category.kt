package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Category
import wallet.type.TransactionCategory

fun TransactionCategory.mapFromDomain(): Category {
    return when (this) {
        TransactionCategory.TOKEN_MINT -> Category.MINT
        TransactionCategory.POST_CREATE -> Category.POST
        TransactionCategory.SHOP_PURCHASE -> Category.PURCHASE
        TransactionCategory.P2P_TRANSFER -> Category.TRANSFER
        TransactionCategory.COMMENT -> Category.COMMENT
        TransactionCategory.LIKE -> Category.LIKE
        TransactionCategory.DISLIKE -> Category.DISLIKE
        TransactionCategory.AD_PINNED -> Category.AD
        TransactionCategory.UNKNOWN__ -> Category.DEFAULT
    }
}
