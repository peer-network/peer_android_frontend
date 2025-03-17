package eu.peernetwork.core.ui.extension

fun Boolean.toInt(): Int {
    return if (this) { 1 } else { 0 }
}
