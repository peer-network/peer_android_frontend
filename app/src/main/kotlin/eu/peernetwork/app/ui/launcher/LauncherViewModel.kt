package eu.peernetwork.app.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.user.domain.interactor.AuthenticationInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

class LauncherViewModel @Inject constructor(
    private val interactor: AuthenticationInteractor,
) : ViewModel() {
    fun reset() {
        viewModelScope.launch {
            interactor.logout()
        }
    }
}
