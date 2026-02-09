package eu.peernetwork.wallet.remote.mock

import eu.peernetwork.core.remote.model.Status
import wallet.wallet.eu.peernetwork.wallet.remote.ResolveTransferMutation

object TransferMock {
    fun transaction(): ResolveTransferMutation.ResolveTransferV2 {
        return ResolveTransferMutation.ResolveTransferV2(
            status = Status.SUCCESS.value,
            ResponseCode = null,
        )
    }
}
