package eu.peernetwork.blog.ui.compose

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import android.graphics.Color.TRANSPARENT
import androidx.core.view.WindowCompat

@Composable
fun LockPortraitWithTransparentSystemUI() {
    val ctx = LocalContext.current
    DisposableEffect(Unit) {
        val activity = ctx as? Activity
        val oldOrientation = activity?.requestedOrientation
        val window = activity?.window
        val oldStatus = window?.statusBarColor
        val oldNav = window?.navigationBarColor

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
            it.statusBarColor = TRANSPARENT
            it.navigationBarColor = TRANSPARENT
        }

        onDispose {
            activity?.requestedOrientation = oldOrientation ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

            window?.let {
                it.statusBarColor = oldStatus ?: TRANSPARENT
                it.navigationBarColor = oldNav ?: TRANSPARENT
                WindowCompat.setDecorFitsSystemWindows(it, true)
            }
        }
    }
}