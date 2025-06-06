package eu.peernetwork.core.ui.extension

fun Boolean.toInt(): Int {
    return if (this) { 1 } else { 0 }
}

fun Boolean.toFloat(): Float {
    return if (this) { 1f } else { 0f }
}
