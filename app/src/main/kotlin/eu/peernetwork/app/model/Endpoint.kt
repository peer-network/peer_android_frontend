package eu.peernetwork.app.model

import com.google.gson.annotations.SerializedName

data class Endpoint(
    @SerializedName("data") val platform: Platform
) {
    data class Platform(
        @SerializedName("android") val configuration: List<Configuration>
    )

    data class Configuration(
        @SerializedName("url") val url: String,
        @SerializedName("version") val version: String,
    )
}
