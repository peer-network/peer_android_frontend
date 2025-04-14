package eu.peernetwork.core.common.usecase

import kotlinx.coroutines.flow.Flow

interface ParameterizedObservableUseCase<P, T> : Usecase {
    operator fun invoke(param: P): Flow<T>
}
