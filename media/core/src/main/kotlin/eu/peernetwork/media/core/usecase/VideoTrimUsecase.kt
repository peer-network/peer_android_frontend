package eu.peernetwork.media.core.usecase

import eu.peernetwork.core.common.usecase.ParameterizedFlowUseCase
import java.io.File

interface VideoTrimUsecase :
    ParameterizedFlowUseCase<VideoTrimUsecase.Parameter, VideoTrimUsecase.Result> {
    data class Parameter(
        val input: String,
        val cacheDir: File,
        val startMs: Long,
        val endMs: Long
    )

    sealed class Result {
        data class Progress(val pct: Int) : Result()
        data class Success(val file: File) : Result()
        data class Failure(val cause: Throwable) : Result()
    }
}