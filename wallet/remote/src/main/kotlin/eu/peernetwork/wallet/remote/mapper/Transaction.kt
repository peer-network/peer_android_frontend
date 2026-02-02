package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Amount
import eu.peernetwork.wallet.domain.model.Category
import eu.peernetwork.wallet.domain.model.Transaction
import wallet.wallet.eu.peernetwork.wallet.remote.TransactionHistoryQuery
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun TransactionHistoryQuery.AffectedRow.mapToDomain(): Transaction {
    return Transaction(
        id = transactionId,
        sender = sender.mapToDomain(),
        recipient = recipient.mapToDomain(),
        message = message,
        category = transactionCategory?.mapFromDomain() ?: Category.DEFAULT,
        amount = Amount(
            net = netTokenAmount.mapToAmount(),
            gross = tokenamount.mapToAmount(),
        ),
        fees = fees?.mapFromDomain(),
        createdAt = createdat.toTimestamp()
    )
}

fun String.toTimestamp(pattern: String = "yyyy-MM-dd HH:mm:ss.SSSSSS"): Long {
    return (SimpleDateFormat(pattern, Locale.getDefault()).parse(this)?.time ?: 0L).run {
        this + TimeZone.getDefault().getOffset(this)
    }
}
