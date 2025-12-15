package eu.peernetwork.app.provider

import android.content.Context
import eu.peernetwork.blog.data.provider.CacheProvider
import java.io.File
import javax.inject.Inject

class CacheProviderDelegate @Inject constructor(
    private val context: Context
) : CacheProvider {
    override fun getCacheDirPath(): File = context.cacheDir
}
