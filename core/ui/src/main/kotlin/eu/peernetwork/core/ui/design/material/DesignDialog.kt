package eu.peernetwork.core.ui.design.material

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import android.view.animation.PathInterpolator
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.extension.isLightTheme
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun  DesignDialog(
    state: State<Boolean>,
    startDestination: String? = null,
    dim: Boolean = false,
    canDismiss: () -> Boolean = { true },
    onBackPressed: () -> Unit = {},
    onShow: () -> Unit = {},
    duration: Long = 250,
    onDismiss: () -> Unit,
    content: @Composable (NavHostController, State<Float>, MutableState<Boolean>) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val isDarkMode = !MaterialTheme.colorScheme.isLightTheme()
    val updatedContent by rememberUpdatedState(content)
    val handleBackPressed by rememberUpdatedState(onBackPressed)
    val handleShow by rememberUpdatedState(onShow)
    val handleDismissRequest by rememberUpdatedState(onDismiss)
    val handleCanDismiss by rememberUpdatedState(canDismiss)
    val cancelable = remember { mutableStateOf(false) }
    val lastState = remember { mutableStateOf(state.value) }
    val session = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val animation = remember { mutableFloatStateOf(0f) }
    val interpolator = remember { PathInterpolator(0.2f, 0f, 0f, 1f) }
    val dialog = remember(session.longValue) {
        object : Dialog(context, R.style.Theme_Peer_Overlay) {
            override fun onBackPressed() {
                if (!handleCanDismiss()) {
                    return
                }
                handleBackPressed()
                if (dispatcher?.hasEnabledCallbacks() != true || cancelable.value) {
                    dismiss()
                } else {
                    dispatcher.onBackPressed()
                }
            }

            override fun show() {
                super.show()
                window?.decorView?.let {
                    ValueAnimator.ofFloat(0f, 1f).apply {
                        this.duration = duration
                        this.interpolator = interpolator
                        addUpdateListener { animation.floatValue = it.animatedValue as Float }
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                handleShow()
                            }
                        })
                    }.start()
                }
            }

            override fun dismiss() {
                handleDismissRequest()
                window?.decorView?.let {
                    ValueAnimator.ofFloat(1f, 0f).apply {
                        this.duration = duration
                        this.interpolator = interpolator
                        addUpdateListener { animation.floatValue = it.animatedValue as Float }
                        addListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                handleDismissal()
                            }
                        })
                    }.start()
                }
            }

            fun handleDismissal() { super.dismiss() }
        }.apply {
            window?.apply {
                setWindowAnimations(0)
                setBackgroundDrawable(null)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                navigationBarColor = Color.TRANSPARENT
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                if (!dim) {
                    clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                }
            }
            setContentView(
                ComposeView(context).apply {
                    setViewTreeLifecycleOwner(lifecycleOwner)
                    setViewTreeViewModelStoreOwner(viewModelStoreOwner)
                    setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
                    setContent {
                        PeerTheme(isDarkMode = isDarkMode) {
                            val controller = rememberNavController()
                            val navBackStackEntry by controller.currentBackStackEntryAsState()
                            val currentStack = remember(navBackStackEntry?.id) {
                                mutableStateOf(controller.currentDestination?.route)
                            }
                            val isStackEmpty = remember(navBackStackEntry?.id) {
                                mutableStateOf(controller.visibleEntries.value.size <= 1)
                            }
                            Box(modifier = Modifier.pointerInput(Unit) {}) {
                                updatedContent(controller, animation, cancelable)
                            }
                            LaunchedEffect(currentStack.value) {
                                cancelable.value = currentStack.value == startDestination || isStackEmpty.value
                            }
                            DisposableEffect(session.longValue) {
                                onDispose {
                                    handleDismissal()
                                    session.longValue = System.currentTimeMillis()
                                }
                            }
                        }
                    }
                }
            )
        }
    }
    LaunchedEffect(state.value) {
        if (state.value) {
            dialog.show()
            lastState.value = true
        } else if (lastState.value) {
            dialog.dismiss()
            lastState.value = false
        }
    }
}
