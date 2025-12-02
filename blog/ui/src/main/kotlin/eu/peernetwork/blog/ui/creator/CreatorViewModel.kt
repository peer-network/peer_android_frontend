package eu.peernetwork.blog.ui.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.ui.model.UiDraft
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.blog.ui.usecase.CreateUsecase
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.model.UiOffset
import eu.peernetwork.media.core.usecase.MediaEncoderUsecase
import eu.peernetwork.media.core.usecase.TrimUsecase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CreatorViewModel @Inject constructor(
    private val usecase: CreateUsecase,
    private val trimUsecase: TrimUsecase,
    private val mediaEncoderUsecase: MediaEncoderUsecase,
) : ViewModel() {
    private val mutableState = MutableStateFlow<State>(State.Empty)

    val state: StateFlow<State> = mutableState.asStateFlow()

    fun create(draft: UiDraft) {
        mutableState.tryEmit(State.Loading)
        viewModelScope.launch {
            try {
                val model = draft.mapToDomain()
                val post = usecase(model)
                mutableState.tryEmit(State.Success(post))
            } catch (error: Throwable) {
                mutableState.tryEmit(State.Error(error))
            }
        }
    }

    private suspend fun UiDraft.mapToDomain(): Draft {
        val media = if (attachment.files.isEmpty()) {
            UiMimeType.Text
        } else {
            attachment.media
        }
        val type = when(media) {
            UiMimeType.Photo -> Draft.Type.Image(attachment.files.map { it.path })
            UiMimeType.Video -> Draft.Type.Video(attachment.files.map {
                val offset = it.props.getParcelable<UiOffset?>(it.path) ?: UiOffset.None
                val url = trimUsecase(TrimUsecase.Parameter(it.uri, offset))
                Draft.Media(url)
            })
            UiMimeType.Music -> Draft.Type.Audio(
                media = attachment.files.map { media ->
                    Draft.Media(
                        url = media.path,
                        cover = media.cover?.let { mediaEncoderUsecase(it) }
                    )
                }
            )
            else -> Draft.Type.Text(listOf(description))
        }
        return Draft(
            title = title,
            description = description,
            tags = description.hashtags(),
            type = type,
        )
    }

    private fun String.hashtags(): List<String> {
        return Regex("#\\w+").findAll(this)
            .map { it.value.replace("#", "") }
            .toList()
    }

    fun reset() {
        viewModelScope.launch {
            mutableState.tryEmit(State.Empty)
        }
    }

    sealed interface State {
        data object Empty : State
        data object Loading : State
        data class Success(val content: UiPost): State
        data class Error(val error: Throwable): State
    }
}
