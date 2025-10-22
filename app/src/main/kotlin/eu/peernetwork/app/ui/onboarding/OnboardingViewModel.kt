package eu.peernetwork.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.model.Properties
import eu.peernetwork.app.usecase.OnboardingUpdateUsecase
import eu.peernetwork.app.usecase.OnboardingUsecase
import eu.peernetwork.user.domain.model.Preference
import eu.peernetwork.user.domain.usecase.PreferenceUpdateUsecase
import eu.peernetwork.user.domain.usecase.PreferenceUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val preferenceUsecase: PreferenceUsecase,
    private val preferenceUpdateUsecase: PreferenceUpdateUsecase,
    private val onboardingUsecase: OnboardingUsecase,
    private val onboardingUpdateUsecase: OnboardingUpdateUsecase
) : ViewModel() {
    private val _status = MutableStateFlow<Status>(Status.Default)

    private val _state = MutableStateFlow<State>(State.Default)

    val status: StateFlow<Status> = _status.asStateFlow()

    val state: StateFlow<State> = _state.asStateFlow()

    fun initialize() {
        viewModelScope.launch {
            try {
                _state.tryEmit(State.Loading)
                val properties = onboardingUsecase()
                _state.tryEmit(State.Success(properties))
            } catch (error: Throwable) {
                _state.tryEmit(State.Error(error))
            }
        }
    }

    fun reset() {
        viewModelScope.launch {
            _status.tryEmit(Status.Default)
        }
    }

    fun finish(status: Boolean) {
        viewModelScope.launch {
            try {
                _status.tryEmit(Status.Loading)
                val preference = preferenceUsecase()
                preferenceUpdateUsecase(preference.copy(
                    flags = ((_state.value as? State.Success?)?.properties ?: onboardingUsecase())
                        .configuration.onboarding.availableOnboardings
                ))
                onboardingUpdateUsecase(status)
                _status.tryEmit(Status.Success(preference))
            } catch (error: Throwable) {
                _status.tryEmit(Status.Error(error))
            }
        }
    }

    sealed interface Status {
        data object Default : Status
        data object Loading : Status
        data class Success(val preference: Preference) : Status
        data class Error(val error: Throwable) : Status
    }

    sealed interface State {
        data object Default : State
        data object Loading : State

        data class Success(val properties: Properties): State
        data class Error(val error: Throwable) : State
    }
}
