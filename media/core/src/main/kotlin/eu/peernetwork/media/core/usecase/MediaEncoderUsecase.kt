package eu.peernetwork.media.core.usecase

import android.net.Uri
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase

interface MediaEncoderUsecase : ParameterizedSuspendableUseCase<Uri, String?>
