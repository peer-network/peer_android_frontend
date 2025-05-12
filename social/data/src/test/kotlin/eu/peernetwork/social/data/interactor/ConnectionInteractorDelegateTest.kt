package eu.peernetwork.social.data.interactor

import eu.peernetwork.social.domain.interactor.ConnectionInteractor
import eu.peernetwork.social.domain.usecase.FollowUsecase
import io.mockk.mockk
import org.junit.Before

internal class ConnectionInteractorDelegateTest {
    private val usecase: FollowUsecase = mockk()

    private lateinit var interactor: ConnectionInteractor

    @Before
    fun setup() {
        interactor = ConnectionInteractorDelegate(usecase)
    }


}
