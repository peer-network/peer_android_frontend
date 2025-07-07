package eu.peernetwork.messaging.ui.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.messaging.ui.model.UiChat
import eu.peernetwork.messaging.ui.design.compose.DesignChatUser
import kotlinx.coroutines.delay

@Composable
fun ChatScreen(
    onChatSelected: (String) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Chat.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = ChatViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    LaunchedEffect(Unit) {
        viewModel.list(pageable = Pageable(offset = 0, limit = 20))
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val chats = remember(state) {
        when (state) {
            is ChatViewModel.State.Success -> (state as ChatViewModel.State.Success).chats
            else -> emptyList()
        }
    }
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                ChatViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                ChatViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is ChatViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as ChatViewModel.State.Success).chats
                    )
                }
                is ChatViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as ChatViewModel.State.Error).error
                )
            }
        }
    }

    DesignStatefulScaffold<List<UiChat>>(
        state = derivedState,
        onRefresh = {
            viewModel.list(pageable = Pageable(offset = 0, limit = 20))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Messages",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                DesignButton(
                    onClick = {
                        val hardcodedUserIds = listOf("b6085cd0-ccc9-449a-9e5d-fa4784280ba2") //Constantine's id
                        val hardcodedUserIdSandro = listOf("42935fcd-4e4e-4d89-944f-bfeb1486fc64")
                        val hardcodedCoconut = listOf("33b86e01-259a-4f2b-8228-6dace8e1dee3")
                        val hardcodedName = ""
                        viewModel.create(hardcodedUserIds, hardcodedName)
                    }
                ) {
                    Text(
                        text = "Start a chat",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Divider()
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(chats) { chat ->
                    DesignChatUser(
                        chat = chat,
                        onClick = {
                            viewModel.get(chat.id, Pageable(offset = 0, limit = 20))
                            onChatSelected(chat.id)
                        }
                    )
                }
            }
        }
    }
}
