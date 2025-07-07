package eu.peernetwork.messaging.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.messaging.domain.usecase.ChatListUsecase
import eu.peernetwork.messaging.domain.usecase.CreateChatUsecase
import eu.peernetwork.messaging.domain.usecase.GetChatUsecase
import eu.peernetwork.messaging.ui.mapper.mapFromDomain
import eu.peernetwork.messaging.ui.model.UiChat
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val getChatUsecase: GetChatUsecase,
    private val createChatUsecase: CreateChatUsecase,
    private val listChatUsecase: ChatListUsecase
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)
    val state: StateFlow<State> = mutableState.asStateFlow()

    fun get(chatId: String, pageable: Pageable) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                val page = getChatUsecase.invoke(
                    GetChatUsecase.GetChatParams(
                        chatId = chatId,
                        pageable = pageable
                    )
                )
                val uiChats = page.items.map { it.mapFromDomain() }
                mutableState.tryEmit(
                    State.Success(
                        chats = uiChats,
                        selectedChatId = chatId
                    )
                )
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun list(pageable: Pageable) {
        viewModelScope.launch {
            mutableState.tryEmit(State.Loading)
            try {
                val page = listChatUsecase.invoke(
                    ChatListUsecase.ChatListParams(pageable = pageable)
                )
                val chatList = page.items.map { it.mapFromDomain() }
                mutableState.tryEmit(
                    if (chatList.isEmpty()) State.Empty
                    else State.Success(chatList)
                )
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    fun create(recipients: List<String>, name: String, image: String? = null) {
        viewModelScope.launch {
            try {
                val newChat = createChatUsecase.invoke(
                    CreateChatUsecase.CreateChatParams(recipients, name, image)
                ).mapFromDomain()
                val currentChats = (mutableState.value as? State.Success)?.chats ?: emptyList()
                mutableState.tryEmit(State.Success(currentChats + newChat))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val chats: List<UiChat>, val selectedChatId: String? = null) : State
        data class Error(val error: Throwable) : State
    }
}
