package eu.peernetwork.user.domain.usecase

import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.model.Coupon
import eu.peernetwork.user.domain.repository.ResourceRepository
import javax.inject.Inject

class CouponUsecase @Inject constructor(
    private val repository: ResourceRepository
) : SuspendableUseCase<List<Coupon>> {
    override suspend fun invoke(): List<Coupon> {
        return repository.coupons()
    }
}
