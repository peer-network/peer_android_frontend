package eu.peernetwork.app.ui.settings

import androidx.compose.runtime.remember
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.DesignTheme
import eu.peernetwork.core.ui.theme.PeerAppRed
import eu.peernetwork.social.ui.feedback.FeedbackScreen
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.deactivate.DeactivateScreen
import eu.peernetwork.user.ui.logout.LogoutScreen
import eu.peernetwork.user.ui.user.UserBadge

@Composable
fun SettingsScreen(
    account: Account,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Settings.Builder::class.java).build(context)
    }
    val accountLabel = stringResource(R.string.account_label)
    val showLogout = remember { mutableStateOf(false) }
    val showDeactivation = remember { mutableStateOf(false) }
    SettingsNavigation(account, component) { backstack, controller ->
        SettingsScreen(
            showLogout = showLogout,
            showDeactivate = showDeactivation,
            onTutorial = { component.settingsEvent().invoke(SettingsEvent.Event.Tutorial) },
            onNavigate = { controller.navigateIfNecessary(it) }
        ) {
            UserBadge(
                id = account.id,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) { controller.navigateIfNecessary(accountLabel) }
        }
        DesignTitleBarHost("SettingsScreen") {
            titleBar {
                DesignTitle {
                    Text(stringResource(eu.peernetwork.core.ui.R.string.settings_label))
                }
            }
        }
        LogoutScreen(
            show = showLogout,
            provider = component,
            viewModelStoreOwner = backstack
        )
        DeactivateScreen(
            show = showDeactivation,
            provider = component,
            viewModelStoreOwner = backstack
        )
    }
}

@Composable
fun SettingsScreen(
    showLogout: MutableState<Boolean>,
    showDeactivate: MutableState<Boolean>,
    onTutorial: () -> Unit,
    onNavigate: (String) -> Unit,
    header: @Composable () -> Unit
) {
    val updateHeader by rememberUpdatedState(header)
    val handleOnNavigate by rememberUpdatedState(onNavigate)
    val referral = stringResource(R.string.referral_name_label)
    val password = stringResource(R.string.password_label)
    val email = stringResource(R.string.email_label)
    val feedback = stringResource(R.string.feedback_label)
    val introduction = stringResource(R.string.how_it_works_label)
    val releaseNote = stringResource(R.string.release_notes)
    val aboutUsLabel = stringResource(R.string.about_us_label)
    val feedbackSession = remember { mutableLongStateOf(-1) }
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)) {
            updateHeader()
        }
        SettingsItem(label = referral) {
            handleOnNavigate(referral)
        }
        SettingsItem(label = password) {
            handleOnNavigate(password)
        }
        SettingsItem(label = email) {
            handleOnNavigate(email)
        }
        SettingsItem(label = feedback) {
            feedbackSession.longValue = System.currentTimeMillis()
        }
        SettingsItem(label = introduction,  onClick = onTutorial)
        SettingsItem(label = releaseNote) {
            handleOnNavigate("version")
        }
        SettingsItem(label = aboutUsLabel) {
            handleOnNavigate("about")
        }
        SettingsItem(
            color = PeerAppRed,
            label = stringResource(R.string.logout_text)
        ) {
            showLogout.value = true
        }
        SettingsItem(
            color = PeerAppRed,
            label = stringResource(R.string.deactivate_text)
        ) {
            showDeactivate.value = true
        }
    }
    FeedbackScreen(feedbackSession, BuildConfig.FEED_BACK)
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSettingsScreen() {
    DesignTheme {
        val showLogout = remember { mutableStateOf(false) }
        val showDeactivation = remember { mutableStateOf(false) }
        SettingsScreen(
            showLogout = showLogout,
            showDeactivate = showDeactivation,
            onTutorial = {},
            onNavigate = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            )
        }
    }
}
