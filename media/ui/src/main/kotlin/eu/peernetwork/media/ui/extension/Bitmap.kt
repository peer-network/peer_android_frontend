package eu.peernetwork.media.ui.extension

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import kotlin.math.abs

fun Bitmap.blur(radius: Int): Bitmap {
    if (radius < 1) return this.copy(this.config ?: Bitmap.Config.ARGB_8888, true)

    val width = this.width
    val height = this.height
    val pixelCount = width * height

    val pixels = IntArray(pixelCount)
    this.getPixels(pixels, 0, width, 0, 0, width, height)

    val widthMinus1 = width - 1
    val heightMinus1 = height - 1
    val diameter = radius + radius + 1

    val redChannel = IntArray(pixelCount)
    val greenChannel = IntArray(pixelCount)
    val blueChannel = IntArray(pixelCount)
    val verticalMin = IntArray(width.coerceAtMost(height))

    var divisionSum = (diameter + 1) shr 1
    divisionSum *= divisionSum
    val divisionTable = IntArray(256 * divisionSum)
    for (i in 0 until 256 * divisionSum) {
        divisionTable[i] = i / divisionSum
    }

    var yIndex = 0
    var yWidth = 0

    val stack = Array(diameter) { IntArray(3) }

    // Horizontal blur pass
    for (y in 0 until height) {
        var redInSum = 0
        var greenInSum = 0
        var blueInSum = 0
        var redOutSum = 0
        var greenOutSum = 0
        var blueOutSum = 0
        var redSum = 0
        var greenSum = 0
        var blueSum = 0

        for (i in -radius..radius) {
            val pixel = pixels[yIndex + i.coerceIn(0, widthMinus1)]
            val stackValue = stack[i + radius]
            stackValue[0] = (pixel shr 16) and 0xff
            stackValue[1] = (pixel shr 8) and 0xff
            stackValue[2] = pixel and 0xff

            val radiusBasedScale = radius + 1 - abs(i)
            redSum += stackValue[0] * radiusBasedScale
            greenSum += stackValue[1] * radiusBasedScale
            blueSum += stackValue[2] * radiusBasedScale

            if (i > 0) {
                redInSum += stackValue[0]
                greenInSum += stackValue[1]
                blueInSum += stackValue[2]
            } else {
                redOutSum += stackValue[0]
                greenOutSum += stackValue[1]
                blueOutSum += stackValue[2]
            }
        }

        var stackPointer = radius

        for (x in 0 until width) {
            redChannel[yIndex] = divisionTable[redSum]
            greenChannel[yIndex] = divisionTable[greenSum]
            blueChannel[yIndex] = divisionTable[blueSum]

            redSum -= redOutSum
            greenSum -= greenOutSum
            blueSum -= blueOutSum

            val stackStart = stackPointer - radius + diameter
            val stackValue = stack[stackStart % diameter]

            redOutSum -= stackValue[0]
            greenOutSum -= stackValue[1]
            blueOutSum -= stackValue[2]

            if (y == 0) {
                verticalMin[x] = (x + radius + 1).coerceAtMost(widthMinus1)
            }
            val pixel = pixels[yWidth + verticalMin[x]]

            stackValue[0] = (pixel shr 16) and 0xff
            stackValue[1] = (pixel shr 8) and 0xff
            stackValue[2] = pixel and 0xff

            redInSum += stackValue[0]
            greenInSum += stackValue[1]
            blueInSum += stackValue[2]

            redSum += redInSum
            greenSum += greenInSum
            blueSum += blueInSum

            stackPointer = (stackPointer + 1) % diameter
            val nextStackValue = stack[stackPointer]

            redOutSum += nextStackValue[0]
            greenOutSum += nextStackValue[1]
            blueOutSum += nextStackValue[2]

            redInSum -= nextStackValue[0]
            greenInSum -= nextStackValue[1]
            blueInSum -= nextStackValue[2]

            yIndex++
        }
        yWidth += width
    }

    // Vertical blur pass
    for (x in 0 until width) {
        var redInSum = 0
        var greenInSum = 0
        var blueInSum = 0
        var redOutSum = 0
        var greenOutSum = 0
        var blueOutSum = 0
        var redSum = 0
        var greenSum = 0
        var blueSum = 0
        var yPosition = -radius * width

        for (i in -radius..radius) {
            val channelIndex = 0.coerceAtLeast(yPosition) + x
            val stackValue = stack[i + radius]
            stackValue[0] = redChannel[channelIndex]
            stackValue[1] = greenChannel[channelIndex]
            stackValue[2] = blueChannel[channelIndex]

            val radiusBasedScale = radius + 1 - abs(i)
            redSum += redChannel[channelIndex] * radiusBasedScale
            greenSum += greenChannel[channelIndex] * radiusBasedScale
            blueSum += blueChannel[channelIndex] * radiusBasedScale

            if (i > 0) {
                redInSum += stackValue[0]
                greenInSum += stackValue[1]
                blueInSum += stackValue[2]
            } else {
                redOutSum += stackValue[0]
                greenOutSum += stackValue[1]
                blueOutSum += stackValue[2]
            }
            if (i < heightMinus1) {
                yPosition += width
            }
        }

        yIndex = x
        var stackPointer = radius

        for (y in 0 until height) {
            pixels[yIndex] = -0x1000000 or
                    (divisionTable[redSum] shl 16) or
                    (divisionTable[greenSum] shl 8) or
                    divisionTable[blueSum]

            redSum -= redOutSum
            greenSum -= greenOutSum
            blueSum -= blueOutSum

            val stackStart = stackPointer - radius + diameter
            val stackValue = stack[stackStart % diameter]

            redOutSum -= stackValue[0]
            greenOutSum -= stackValue[1]
            blueOutSum -= stackValue[2]

            if (x == 0) {
                verticalMin[y] = (y + radius + 1).coerceAtMost(heightMinus1) * width
            }
            val channelPosition = x + verticalMin[y]

            stackValue[0] = redChannel[channelPosition]
            stackValue[1] = greenChannel[channelPosition]
            stackValue[2] = blueChannel[channelPosition]

            redInSum += stackValue[0]
            greenInSum += stackValue[1]
            blueInSum += stackValue[2]

            redSum += redInSum
            greenSum += greenInSum
            blueSum += blueInSum

            stackPointer = (stackPointer + 1) % diameter
            val nextStackValue = stack[stackPointer]

            redOutSum += nextStackValue[0]
            greenOutSum += nextStackValue[1]
            blueOutSum += nextStackValue[2]

            redInSum -= nextStackValue[0]
            greenInSum -= nextStackValue[1]
            blueInSum -= nextStackValue[2]

            yIndex += width
        }
    }
    val blurredBitmap = createBitmap(width, height, this.config ?: Bitmap.Config.ARGB_8888)
    blurredBitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return blurredBitmap
}
