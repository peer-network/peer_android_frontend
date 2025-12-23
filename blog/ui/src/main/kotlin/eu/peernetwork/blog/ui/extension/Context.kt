package eu.peernetwork.blog.ui.extension

import android.content.Context
import android.content.Intent

fun Context.share(url: String, title: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, url)
    }
    val chooser = Intent.createChooser(
        shareIntent,
        title
    )
    startActivity(chooser)
}
