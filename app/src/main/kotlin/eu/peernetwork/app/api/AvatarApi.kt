package eu.peernetwork.app.api

import android.net.Uri
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.api.AvatarSettingsApi
import javax.inject.Inject

class AvatarApi @Inject constructor(
    private val api: AvatarSettingsApi,
    private val mediaEncoderUsecase: MediaEncoderUsecase
) : SettingsApi.Attribute<Uri> {
    override suspend fun invoke(value: Uri) {
        mediaEncoderUsecase(value)?.let { api(it) }
    }
}
