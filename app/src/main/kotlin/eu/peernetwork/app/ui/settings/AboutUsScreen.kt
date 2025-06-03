package eu.peernetwork.app.ui.settings

import eu.peernetwork.app.BuildConfig
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R

@Composable
fun AboutUsScreen() {
    val versionName = BuildConfig.VERSION_NAME
    val versionCode = BuildConfig.VERSION_CODE

    DesignTitleBarHost("AboutUs") {
        titleBar {
            DesignTitle {
                Text(
                    text = stringResource(R.string.about_us_label),
                    // style = MaterialTheme.typography.headlineSmall,
                    // color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Icon(
            painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
            contentDescription = stringResource(R.string.about_us_label),
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Nested Column for left-aligned text under the icon
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "\"${stringResource(R.string.about_us_description_label)}\"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text =  stringResource(R.string.app_information_label),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text =  stringResource(R.string.app_name_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${stringResource(R.string.version_text_label)} $versionName ($versionCode)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text =  stringResource(R.string.developed_by_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text =  stringResource(R.string.copyright_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewAboutUsScreen() {
    PeerTheme {
        AboutUsScreen()
    }
}
