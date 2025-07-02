package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.media.core.model.UiMediaData

interface MetaDataUsecase : ParameterizedSuspendableUseCase<String, UiMediaData?>
