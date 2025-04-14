package eu.peernetwork.core.common.usecase

interface ParameterizedImmediateUseCase<P, T> : Usecase {
    operator fun invoke(param: P): T
}
