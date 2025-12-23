package eu.peernetwork.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.interactor.SettingsInteractor
import eu.peernetwork.persistence.domain.publishable.PublishableInteger
import eu.peernetwork.persistence.domain.retrievable.RetrievableInteger
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.usecase.PreferenceUsecase
import eu.peernetwork.user.domain.usecase.PrincipalUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Home.Scope
class HomeViewModel @Inject constructor(
    private val usecase: PrincipalUsecase,
    private val preferenceUsecase: PreferenceUsecase,
    private val interactor: SettingsInteractor,
    private val retrievableInteger: RetrievableInteger,
    private val publishableInteger: PublishableInteger,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    operator fun invoke() {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val preference = preferenceUsecase()
                interactor.setMode(preference.mode.value)
                val principal = usecase(false)
                val lastVisitedPage = retrievableInteger(TAG) ?: 0
                interactor.setUser(principal.id)
                mutableState.tryEmit(State.Success(
                    account = principal,
                    lastVisitedPage = lastVisitedPage,
                    preference = preference
                ))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun lastVisited(page: Int) {
        viewModelScope.launch { publishableInteger(TAG, page) }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(
            val account: Account,
            val lastVisitedPage: Int,
            val preference: Preference
        ) : State
        data class Error(val error: Throwable) : State
    }

    internal companion object {
        val TAG: String = HomeViewModel::class.java.name
    }
}
