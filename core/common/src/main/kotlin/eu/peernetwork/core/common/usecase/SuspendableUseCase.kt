package eu.peernetwork.core.common.usecase

interface SuspendableUseCase<T> : Usecase {
    suspend operator fun invoke(): T
}
