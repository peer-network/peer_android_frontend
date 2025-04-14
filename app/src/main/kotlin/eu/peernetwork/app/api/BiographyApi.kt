package eu.peernetwork.app.api

import eu.peernetwork.core.common.usecase.TextEncoderUsecase
import eu.peernetwork.user.data.api.SettingsApi
import eu.peernetwork.user.remote.api.BiographySettingsApi
import javax.inject.Inject

class BiographyApi @Inject constructor(
    private val api: BiographySettingsApi,
    private val textEncoderUsecase: TextEncoderUsecase
) : SettingsApi.Updatable<String> {
    override suspend fun invoke(value: String) {
        api(textEncoderUsecase(value))
    }
}
