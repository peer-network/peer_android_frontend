package eu.peernetwork.core.common.usecase

interface ImmediateUseCase<T> : Usecase {
    operator fun invoke(): T
}
