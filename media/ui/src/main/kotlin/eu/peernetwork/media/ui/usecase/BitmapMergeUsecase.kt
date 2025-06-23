package eu.peernetwork.media.ui.usecase

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import javax.inject.Inject
import androidx.core.graphics.scale

class BitmapMergeUsecase @Inject constructor() : ParameterizedSuspendableUseCase<BitmapMergeUsecase.Parameter, Bitmap> {
    override suspend fun invoke(param: Parameter): Bitmap {
        val targetHeight = (param.width / param.aspectRatio).toInt()
        val ratio = param.foreground.width.toFloat() / param.foreground.height
        val scaleWidth: Int
        val scaleHeight: Int
        if (ratio > param.aspectRatio) {
            scaleWidth = param.width
            scaleHeight = (param.width / ratio).toInt()
        } else {
            scaleHeight = targetHeight
            scaleWidth = (targetHeight * ratio).toInt()
        }
        val scaledForeground = param.foreground.scale(scaleWidth, scaleHeight)
        val result = param.background.scale(param.width, targetHeight)
        val overlay = result.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(result)
        val paint = Paint()
        val matrix = ColorMatrix().apply {
            setScale(0.7f, 0.7f, 0.7f, 1f)
        }
        paint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(overlay, 0f, 0f, paint)

        val left = ((param.width - scaleWidth) / 2).toFloat()
        val top = ((targetHeight - scaleHeight) / 2).toFloat()

        canvas.drawBitmap(scaledForeground, left, top, null)

        return result
    }

    data class Parameter(
        val width: Int,
        val aspectRatio: Float,
        val background: Bitmap,
        val foreground: Bitmap
    )
}
