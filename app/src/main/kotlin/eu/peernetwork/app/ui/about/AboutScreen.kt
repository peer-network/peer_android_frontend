package eu.peernetwork.app.ui.about

import android.graphics.Bitmap
import android.graphics.Color
import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.referral.Referral
import eu.peernetwork.social.ui.referral.ReferralViewModel
import eu.peernetwork.user.ui.R

@Composable
fun AboutScreen(
    version: String,
    versionCode: Int,
    provider: UiComponentProvider?,
    title: String? = null,
    showReferralQr: Boolean = true,
    viewModelStoreOwner: ViewModelStoreOwner? = LocalViewModelStoreOwner.current,
    qrDpSize: Int = 300,
    qrMatrixSizePx: Int? = null,
    trackingParam: String? = "source=peer_qr",
    qualityMultiplier: Float = 1.4f
) {
    val context = LocalContext.current
    val component = remember(provider) {
        provider?.builder(About.Builder::class.java)?.build(context)
    }

    AboutContent(
        version = version,
        versionCode = versionCode,
        provider = provider,
        showReferralQr = showReferralQr,
        viewModelStoreOwner = viewModelStoreOwner,
        qrDpSize = qrDpSize,
        qrMatrixSizePx = qrMatrixSizePx,
        trackingParam = trackingParam,
        qualityMultiplier = qualityMultiplier
    )

    if (title != null && component != null) {
        DesignTitleBarHost(component.urlInteractor().get()) {
            titleBar { DesignTitle { Text(title) } }
        }
    }
}

@Composable
private fun AboutContent(
    version: String,
    versionCode: Int,
    provider: UiComponentProvider?,
    showReferralQr: Boolean,
    viewModelStoreOwner: ViewModelStoreOwner?,
    qrDpSize: Int,
    qrMatrixSizePx: Int?,
    trackingParam: String?,
    qualityMultiplier: Float
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
    ) {
        Icon(
            painter = painterResource(id = eu.peernetwork.app.R.drawable.ic_icon),
            contentDescription = stringResource(R.string.about_us_label),
            modifier = Modifier.size(52.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.about_us_description_label),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.app_information_label),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.app_name_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.version_text_label, version, versionCode),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.developed_by_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.copyright_label),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.tertiary
            )

            if (showReferralQr && viewModelStoreOwner != null && provider != null) {
                ReferralQrImage(
                    provider = provider,
                    viewModelStoreOwner = viewModelStoreOwner,
                    qrDpSize = qrDpSize,
                    qrMatrixSizePx = qrMatrixSizePx,
                    trackingParam = trackingParam,
                    qualityMultiplier = qualityMultiplier
                )
            }

            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
private fun ReferralQrImage(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    qrDpSize: Int,
    qrMatrixSizePx: Int?,
    trackingParam: String?,
    qualityMultiplier: Float
) {
    val context = LocalContext.current
    val component = remember(provider) {
        provider.builder(Referral.Builder::class.java).build(context)
    }
    val vm = viewModel(
        modelClass = ReferralViewModel::class.java,
        factory = component.viewModelFactory(),
        viewModelStoreOwner = viewModelStoreOwner
    )
    val inviteState by vm.invite.collectAsState()

    LaunchedEffect(inviteState) {
        if (inviteState is ReferralViewModel.Status.Empty) vm.invite()
    }

    val targetSizeDp = qrDpSize.dp
    val density = androidx.compose.ui.platform.LocalDensity.current

    Crossfade(
        targetState = inviteState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        label = "referral_qr_xfade"
    ) { state ->
        when (state) {
            ReferralViewModel.Status.Empty,
            ReferralViewModel.Status.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(targetSizeDp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(strokeWidth = 2.dp)
                }
            }
            is ReferralViewModel.Status.Success -> {
                val rawLink = state.invite.link
                val finalLink = remember(rawLink, trackingParam) {
                    if (trackingParam.isNullOrBlank()) rawLink
                    else if (rawLink.contains("source=")) rawLink
                    else rawLink + (if (rawLink.contains("?")) "&" else "?") + trackingParam
                }

                val matrixSize = remember(finalLink, qrMatrixSizePx, qrDpSize, qualityMultiplier) {
                    qrMatrixSizePx ?: with(density) {
                        (qrDpSize * density.density * qualityMultiplier)
                            .toInt()
                            .coerceAtLeast(512)
                            .coerceAtMost(4096)
                    }
                }

                val colors = MaterialTheme.colorScheme
                val qrBitmap = remember(finalLink, matrixSize, colors) {
                    InFileQrCodeGenerator.bitmap(
                        content = finalLink,
                        size = matrixSize,
                        dark = colors.onBackground.toArgb(),
                        light = colors.background.toArgb()
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "Referral QR",
                        modifier = Modifier.size(targetSizeDp)
                    )
                }
            }
            is ReferralViewModel.Status.Error -> {}
        }
    }
}

private object InFileQrCodeGenerator {
    private val cache = mutableMapOf<String, Bitmap>()

    fun bitmap(
        content: String,
        size: Int,
        dark: Int = Color.BLACK,
        light: Int = Color.WHITE
    ): Bitmap {
        cache[content]?.let { return it }
        val matrix = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = createBitmap(size, size, Bitmap.Config.ARGB_8888)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp[x, y] = if (matrix[x, y]) dark else light
            }
        }
        cache[content] = bmp
        return bmp
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewAboutScreen() {
    PeerTheme {
        AboutContent(
            version = "1.0.0",
            versionCode = 24,
            provider = null,
            showReferralQr = false,
            viewModelStoreOwner = null,
            qrDpSize = 300,
            qrMatrixSizePx = null,
            trackingParam = "source=about_qr",
            qualityMultiplier = 1.4f
        )
    }
}