package eu.peernetwork.wallet.ui.model

data class UiTax(
    val burn: Double,
    val pool: Double,
    val peer: Double,
    val percentage: Double
) {
    companion object {
        val Free = UiTax(
            burn = 0.0,
            pool = 0.0,
            peer = 0.0,
            percentage = 0.0
        )
    }
}
