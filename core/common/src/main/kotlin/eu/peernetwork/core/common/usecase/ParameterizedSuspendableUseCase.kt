package eu.peernetwork.core.common.usecase

interface ParameterizedSuspendableUseCase<P, T> : Usecase {
    suspend operator fun invoke(param: P): T
}
