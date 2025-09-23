package eu.peernetwork.app.model

import com.google.gson.annotations.SerializedName

data class Properties(
    @SerializedName("data") val configuration: Configuration
) {
    data class Configuration(
        @SerializedName("ONBOARDING") val onboarding: Onboarding,
        @SerializedName("DAILY_FREE") val dailyFree: DailyFree,
        @SerializedName("TOKENOMICS") val tokenomics: Tokenomics,
        @SerializedName("MINTING") val minting: Minting
    )

    data class Onboarding(
        @SerializedName("AVAILABLE_ONBOARDINGS") val availableOnboardings: List<String>
    )

    data class DailyFree(
        @SerializedName("DAILY_FREE_ACTIONS") val dailyFreeActions: Map<String, Int>
    )

    data class Tokenomics(
        @SerializedName("ACTION_TOKEN_PRICES") val actionTokenPrices: Map<String, Int>,
        @SerializedName("ACTION_GEMS_RETURNS") val actionGemsReturns: Map<String, Double>
    )

    data class Minting(
        @SerializedName("DAILY_NUMBER_TOKEN") val dailyNumberToken: Int
    )
}
