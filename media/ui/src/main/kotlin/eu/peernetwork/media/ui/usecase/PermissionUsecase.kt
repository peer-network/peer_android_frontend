package eu.peernetwork.media.ui.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import eu.peernetwork.core.common.usecase.ImmediateUseCase
import javax.inject.Inject

class PermissionUsecase @Inject constructor(
    private val context: Context
) : ImmediateUseCase<Unit> {
    override fun invoke() {
        context.startActivity(
            Intent(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
        )
    }
}
