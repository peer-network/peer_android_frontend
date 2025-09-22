package eu.peernetwork.media.ui.interactor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File

class DiskCacheInteractor(
    context: Context,
    directory: String,
    cacheDir: File = File(context.cacheDir, directory),
    private val diskCache: DiskLruCache = DiskLruCache.open(cacheDir, 1, 1, 50L * 1024 * 1024)
) : BitmapInteractor {

    override fun get(key: String): Bitmap? {
        val snapshot = diskCache.get(key.hashCode().toString()) ?: return null
        val input = snapshot.getInputStream(0)
        val bitmap = BitmapFactory.decodeStream(input)
        input.close()
        return bitmap
    }

    override fun put(key: String, bitmap: Bitmap) {
        val editor = diskCache.edit(key.hashCode().toString()) ?: return
        val out = editor.newOutputStream(0)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.close()
        editor.commit()
    }
}
