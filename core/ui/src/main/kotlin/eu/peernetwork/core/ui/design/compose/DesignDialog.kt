package eu.peernetwork.core.ui.design.compose

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Dialog
import android.view.WindowManager
import android.view.animation.PathInterpolator
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    tag: String,
    visible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    handleBackPress: Boolean = true,
    durationMillis: Int = DefaultDurationMillis,
    enter: EnterTransition = fadeIn(animationSpec = tween(durationMillis = durationMillis)),
    exit: ExitTransition = fadeOut(animationSpec = tween(durationMillis = durationMillis)),
    contentAlignment: Alignment = Alignment.TopStart,
    onDismiss: () -> Unit = {},
    onAnimationComplete: (Boolean) -> Unit = {},
    background: (@Composable () -> Unit)? = null,
    builder: @Composable (State<Boolean>) -> Unit,
) {
    val updatedBuilder by rememberUpdatedState(builder)
    val updatedBackground by rememberUpdatedState(background)
    DesignOverlayHost(
        tag = tag,
        visible = visible,
        handleBackPress = handleBackPress,
        onDismiss = onDismiss,
        onAnimationComplete = onAnimationComplete,
        durationMillis = durationMillis
    ) { dialogState ->
        overlay {
            Box(
                modifier = modifier,
                contentAlignment = contentAlignment
            ) {
                updatedBackground?.invoke() ?: DesignOverlayBackground(
                    state = dialogState,
                    durationMillis = durationMillis,
                    modifier = Modifier.fillMaxSize()
                )
                AnimatedVisibility(
                    visible = dialogState.value,
                    enter = enter,
                    exit = exit
                ) { updatedBuilder(dialogState) }
            }
        }
    }
}

@Composable
fun  DesignDialog(
    tag: String,
    state: State<Boolean>,
    behind: Boolean = false,
    duration: Long = 350,
    onDismissRequest: () -> Unit,
    content: @Composable (NavHostController, State<Float>, MutableState<Boolean>) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val isDarkMode = !MaterialTheme.colorScheme.isLightTheme()
    val updatedContent by rememberUpdatedState(content)
    val handleDismissRequest by rememberUpdatedState(onDismissRequest)
    val cancelable = remember { mutableStateOf(false) }
    val session = remember { mutableLongStateOf(System.currentTimeMillis()) }
    val animation = remember { mutableFloatStateOf(0f) }
    val interpolator = remember { PathInterpolator(0.2f, 0f, 0f, 1f) }
    val dialog = remember(session.longValue) {
        object : Dialog(context, R.style.Theme_Peer_Overlay) {
            override fun onBackPressed() {
                if (dispatcher?.hasEnabledCallbacks() != true || cancelable.value) {
                    dismiss()
                } else {
                    dispatcher.onBackPressed()
                }
            }

            override fun show() {
                super.show()
                window?.decorView?.let {
                    ObjectAnimator.ofFloat(it, "alpha", 0f, 1f).apply {
                        this.duration = duration
                        this.interpolator = interpolator
                        addUpdateListener { animation.floatValue = it.animatedValue as Float }
                    }.start()
                }
            }

            override fun dismiss() {
                handleDismissRequest()
                window?.decorView?.let {
                    ObjectAnimator.ofFloat(it, "alpha", 1f, 0f).apply {
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
                if (!behind) {
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
                            DesignOverlay {
                                DesignTitleBar {
                                    val controller = rememberNavController()
                                    val navBackStackEntry by controller.currentBackStackEntryAsState()
                                    val currentStack = remember(navBackStackEntry?.id) {
                                        mutableStateOf(controller.currentDestination?.route)
                                    }
                                    val isStackEmpty = remember(navBackStackEntry?.id) {
                                        mutableStateOf(controller.visibleEntries.value.size <= 1)
                                    }
                                    updatedContent(controller, animation, cancelable)
                                    LaunchedEffect(currentStack.value) {
                                        cancelable.value = currentStack.value == tag || isStackEmpty.value
                                    }
                                    DisposableEffect(session.longValue) {
                                        onDispose {
                                            session.longValue = System.currentTimeMillis()
                                        }
                                    }
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
        } else {
            dialog.dismiss()
        }
    }
}
