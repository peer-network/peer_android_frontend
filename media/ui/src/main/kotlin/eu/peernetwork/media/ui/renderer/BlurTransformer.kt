package eu.peernetwork.media.ui.renderer

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import coil.size.Size
import coil.transform.Transformation
import eu.peernetwork.media.core.interactor.ThumbnailInteractor
import javax.inject.Inject

class BlurTransformer @Inject constructor(
    private val interactor: ThumbnailInteractor
) : Transformation {

    override val cacheKey: String = BlurTransformer::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val targetWidth = (input.width * 0.3).toInt()
        val targetHeight = (input.height * 0.3).toInt()
        val blurred = interactor.blur(input, 5)
        val scaled = blurred.scale(targetWidth, targetHeight)
        val result =
            createBitmap(scaled.width, scaled.height, scaled.config ?: Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        canvas.drawBitmap(scaled, 0f, 0f, null)
        val paint = Paint().apply {
            color = Color.BLACK
            alpha = 30
        }
        canvas.drawRect(0f, 0f, scaled.width.toFloat(), scaled.height.toFloat(), paint)
        return result
    }
}