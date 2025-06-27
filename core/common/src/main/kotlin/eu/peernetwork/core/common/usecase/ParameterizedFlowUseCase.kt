package eu.peernetwork.core.common.usecase

import kotlinx.coroutines.flow.Flow

interface ParameterizedFlowUseCase<P, R> {
    operator fun invoke(param: P): Flow<R>
}