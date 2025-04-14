package eu.peernetwork.media.core.usecase

import android.net.Uri
import eu.peernetwork.core.common.usecase.ParameterizedImmediateUseCase

interface MediaEncoderUsecase : ParameterizedImmediateUseCase<Uri, String?>
