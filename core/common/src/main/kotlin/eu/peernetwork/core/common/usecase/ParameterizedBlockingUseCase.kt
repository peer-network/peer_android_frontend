package eu.peernetwork.core.common.usecase

interface ParameterizedBlockingUseCase<P, T> : Usecase {
    operator fun invoke(param: P): T
}
