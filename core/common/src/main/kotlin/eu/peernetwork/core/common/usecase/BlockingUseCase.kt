package eu.peernetwork.core.common.usecase

interface BlockingUseCase<T> : Usecase {
    operator fun invoke(): T
}
