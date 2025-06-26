package eu.peernetwork.media.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.yalantis.ucrop.UCropActivity

class CropActivity : UCropActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val statusBarHeight = getStatusBarHeight(this)
        val rootView = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
        rootView?.setPadding(0, statusBarHeight, 0, 0)
    }

    private fun getStatusBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }
}