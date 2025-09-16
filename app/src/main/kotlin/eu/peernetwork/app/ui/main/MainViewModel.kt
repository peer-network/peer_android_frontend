package eu.peernetwork.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.interceptor.SubscriptionInteractor
import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import eu.peernetwork.user.domain.model.Token
import eu.peernetwork.user.domain.usecase.PrincipalUsecase
import eu.peernetwork.user.domain.usecase.TokenObserverUsecase
import eu.peernetwork.user.domain.usecase.TokenUsecase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@Main.Scope
class MainViewModel @Inject constructor(
    tokenUsecase: TokenUsecase,
    tokenObserverUsecase: TokenObserverUsecase,
    private val usecase: PrincipalUsecase,
    private val interactor: SubscriptionInteractor,
    preferences: PreferenceRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<State> = tokenObserverUsecase()
        .mapLatest { token ->
            token?.let {
                interactor.subscribe(usecase())
            } ?: interactor.unSubscribe()
            State(token)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = State(tokenUsecase())
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val onboardingCompleted: StateFlow<Boolean> = preferences
        .observe("onboarding_completed", Boolean::class.java)
        .map { it ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), preferences.get("onboarding_completed", Boolean::class.java) ?: false)

    data class State(val token: Token?)
}