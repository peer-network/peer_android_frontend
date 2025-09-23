package eu.peernetwork.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.app.usecase.OnboardingUsecase
import eu.peernetwork.persistence.domain.repository.PreferenceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class OnboardingViewModel @Inject constructor(
    private val preferences: PreferenceRepository,
    private val onboardingUsecase: OnboardingUsecase
) : ViewModel() {
    companion object {
        const val KEY = "onboarding_completed"
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val completed: StateFlow<Boolean> = preferences
        .observe(KEY, Boolean::class.java)
        .map { it ?: false }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), preferences.get(KEY, Boolean::class.java) ?: false)

    fun complete() {
        viewModelScope.launch {
            preferences.set(KEY, true)
        }
    }
}