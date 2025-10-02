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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.feedback.FeedbackScreen
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.settings.account.AccountPreview

@Composable
fun SettingsScreen(
    userId: String,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Settings.Builder::class.java).build(context)
    }
    val account = stringResource(R.string.account_label)
    SettingsNavigation(userId, component, viewModelStore) { controller ->
        SettingsScreen({
            component.settingsEvent().invoke(SettingsEvent.Event.Tutorial)
        }, { controller.navigateIfNecessary(it) }) {
            AccountPreview(component, viewModelStore.get(userId)) {
                controller.navigateIfNecessary(account)
            }
        }
        DesignTitleBarHost("SettingsScreen") {
            titleBar {
                DesignTitle {
                    Text(stringResource(eu.peernetwork.core.ui.R.string.settings_label))
                }
            }
        }
    }
}

@Composable
fun SettingsScreen(
    onTutorial: () -> Unit,
    onNavigate: (String) -> Unit,
    header: @Composable () -> Unit
) {
    val updateHeader by rememberUpdatedState(header)
    val handleOnNavigate by rememberUpdatedState(onNavigate)
    val referral = stringResource(R.string.referral_name_label)
    val password = stringResource(R.string.password_label)
    val preference = stringResource(R.string.preference_label)
    val feedback = stringResource(R.string.feedback_label)
    val introduction = stringResource(R.string.how_it_works_label)
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
        SettingsItem(label = preference) {
            handleOnNavigate(preference)
        }
        SettingsItem(label = feedback) {
            feedbackSession.longValue = System.currentTimeMillis()
        }
        SettingsItem(label = introduction, onTutorial)
        SettingsItem(label = aboutUsLabel) {
            handleOnNavigate("about")
        }
    }
    FeedbackScreen(
        feedbackSession,
        BuildConfig.APPLICATION_ID
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewSettingsScreen() {
    PeerTheme {
        SettingsScreen({}, {}) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
    }
}
