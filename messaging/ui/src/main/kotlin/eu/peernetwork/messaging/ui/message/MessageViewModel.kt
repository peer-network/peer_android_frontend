package eu.peernetwork.messaging.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.messaging.domain.usecase.DeleteMessageUsecase
import eu.peernetwork.messaging.domain.usecase.GetMessageUsecase
import eu.peernetwork.messaging.domain.usecase.SendMessageUsecase
import eu.peernetwork.messaging.ui.mapper.mapFromDomain
import eu.peernetwork.messaging.ui.model.UiMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MessageViewModel @Inject constructor(
    private val getMessageUsecase: GetMessageUsecase,
    private val sendMessageUsecase: SendMessageUsecase,
    private val deleteMessageUsecase: DeleteMessageUsecase
): ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    private val lastUpdated = MutableStateFlow(System.currentTimeMillis())
    val state: StateFlow<State> = mutableState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun get(chatId: String) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                lastUpdated.flatMapLatest { getMessageUsecase(chatId) }
                    .collectLatest { messages ->
                        val uiMessages = messages.map { it.mapFromDomain() }
                        mutableState.emit(State.Success(uiMessages))
                    }
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun watch() {
        viewModelScope.launch {
            while (true) {
                lastUpdated.tryEmit(System.currentTimeMillis())
                delay(10_000)
            }
        }
    }

    fun send(chatId: String, content: String) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                sendMessageUsecase(
                    SendMessageUsecase.SendMessageParams(
                        chatId = chatId,
                        content = content
                    )
                )
                mutableState.tryEmit(State.Success(getMessageUsecase(chatId).first().map { it.mapFromDomain() }))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun delete(chatId: String, messId: Int) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                deleteMessageUsecase(
                    DeleteMessageUsecase.DeleteMessageParams(
                        chatId = chatId,
                        messId = messId
                    )
                )
                mutableState.tryEmit(State.Success(getMessageUsecase(chatId).first().map { it.mapFromDomain() }))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val chats: List<UiMessage>) : State
        data class Error(val error: Throwable) : State
    }
}