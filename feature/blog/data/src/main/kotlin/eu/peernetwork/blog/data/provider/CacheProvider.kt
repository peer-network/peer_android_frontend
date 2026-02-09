package eu.peernetwork.blog.data.provider

import java.io.File

interface CacheProvider {
    fun getCacheDirPath(): File
}
