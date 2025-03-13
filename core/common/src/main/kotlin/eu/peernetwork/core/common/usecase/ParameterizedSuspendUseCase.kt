package eu.peernetwork.core.common.usecase

interface ParameterizedSuspendUseCase<P, T> : Usecase {
    suspend operator fun invoke(param: P): T
}
