package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedImmediateUseCase
import eu.peernetwork.media.core.model.UiMediaData

interface MetaDataUsecase : ParameterizedImmediateUseCase<String, UiMediaData?>
