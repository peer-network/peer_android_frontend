package eu.peernetwork.app.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponent
import eu.peernetwork.core.ui.extension.findBuilder
import eu.peernetwork.core.ui.theme.DesignTheme
import javax.inject.Inject

class MainActivity : ComponentActivity(), UiComponent.Provider<Main.Component> {
    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private lateinit var viewModel: MainViewModel

    override val injector: Main.Component by lazy {
        findBuilder(Main.Builder::class).build(this)
    }

    @Suppress("KotlinConstantConditions")
    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DesignTheme(
                isDarkMode = if (BuildConfig.USE_SYSTEM_THEME) {
                    isSystemInDarkTheme()
                } else {
                    true
                }
            ) {
                MainScreen(
                    component = injector,
                    viewModelStoreOwner = this
                )
            }
        }
    }
}
