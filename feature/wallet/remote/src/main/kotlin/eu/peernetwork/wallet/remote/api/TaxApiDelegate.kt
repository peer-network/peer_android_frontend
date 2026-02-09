package eu.peernetwork.wallet.remote.api

import com.google.gson.Gson
import eu.peernetwork.core.remote.exception.NetworkException
import eu.peernetwork.wallet.data.api.TaxApi
import eu.peernetwork.wallet.domain.exception.WalletException
import eu.peernetwork.wallet.domain.model.Tax
import eu.peernetwork.wallet.remote.mapper.mapToDomain
import eu.peernetwork.wallet.remote.model.TaxModel
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Named

class TaxApiDelegate @Inject constructor(
    private val gson: Gson,
    @Named("mediaUrl") private val url: String,
    private val client: OkHttpClient
) : TaxApi {
    override suspend fun getTax(): Tax {
        val request = Request.Builder().url("$url/assets/constants.json").build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw NetworkException(response.message)
        }
        return response.body?.string()?.let {
            gson.fromJson(it, TaxModel::class.java).mapToDomain()
        } ?: throw WalletException()
    }
}
