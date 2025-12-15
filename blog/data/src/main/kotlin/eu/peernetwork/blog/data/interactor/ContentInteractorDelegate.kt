package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.data.provider.CacheProvider
import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import eu.peernetwork.blog.domain.repository.MultipartRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import java.io.File
import javax.inject.Inject

class ContentInteractorDelegate @Inject constructor(
    private val cache: CacheProvider,
    private val repository: ContentRepository,
    private val multipartRepository: MultipartRepository,
    private val eligibilityRepository: EligibilityRepository,
    private val rewardRepository: RewardRepository
) : ContentInteractor {
    override suspend fun create(draft: Draft): Content {
        val token = eligibilityRepository.get()
        val meta = when (draft.type) {
            is Draft.Type.Text -> {
                val paths = (draft.type as Draft.Type.Text).files.map { content ->
                    val name = String.format("%s.txt", System.currentTimeMillis().toString())
                    val file = File(cache.getCacheDirPath(), name)
                    if (!file.exists()) {
                        file.parentFile?.mkdirs()
                        file.createNewFile()
                    }
                    file.writeText(content)
                    file
                }
                multipartRepository.upload(token, paths.map { it.path }).apply {
                    paths.map { it.deleteOnExit() }
                }
            }
            is Draft.Type.Image -> {
                val paths = (draft.type as Draft.Type.Image).files
                multipartRepository.upload(token, paths)
            }
            is Draft.Type.Video -> {
                val paths = (draft.type as Draft.Type.Video).media.map { it.url }
                multipartRepository.upload(token, paths)
            }
            is Draft.Type.Audio -> {
                val paths = (draft.type as Draft.Type.Audio).media.map { it.url }
                multipartRepository.upload(token, paths)
            }
        }
        val content = repository.create(draft, meta)
        try {
            rewardRepository.get()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return content
    }
}
