package eu.peernetwork.wallet.remote.mapper

import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.remote.model.TaxModel

fun TaxModel.mapToDomain(): Tax {
    return Tax(
        burn = this.data.item.value.burn,
        pool = this.data.item.value.pool,
        peer = this.data.item.value.peer,
        percentage = this.data.item.value.invitation,
    )
}
