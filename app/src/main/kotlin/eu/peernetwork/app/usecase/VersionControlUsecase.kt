package eu.peernetwork.app.usecase

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.service.NetworkResource
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class VersionControlUseCase @Inject constructor(
    private val provider: NetworkResource,
    private val remoteConfig: FirebaseRemoteConfig
) : SuspendableUseCase<VersionControlUseCase.Result> {

    override suspend fun invoke(): Result {
        return try {
            remoteConfig.fetch(0).await()
            remoteConfig.activate().await()
            val json = remoteConfig.getString("minimum_required_version")
            Log.d("VersionControl", "Fetched minimum_required_version JSON: $json")

            val listType = object : TypeToken<List<MinimumRequiredVersion>>() {}.type
            val versionList: List<MinimumRequiredVersion> = Gson().fromJson(json, listType)
            val minimumVersion = versionList.firstOrNull()

            val currentVersion = BuildConfig.VERSION_NAME
            Log.d("VersionControl", "Current app version: $currentVersion")
            Log.d("VersionControl", "Minimum required version from remote config: ${minimumVersion?.version}")
            Log.d("VersionControl", "Base URL from remote config: ${minimumVersion?.url}")

            minimumVersion?.url?.let {
                Log.d("VersionControl", "Updating provider baseUrl to: $it")
                provider.baseUrl(it)
            } ?: Log.d("VersionControl", "No base URL found in minimum required version")

            return if (minimumVersion != null && isOutdated(currentVersion, minimumVersion.version)) {
                Log.d("VersionControl", "App version is outdated")
                Result.Outdated(minimumVersion.url)
            } else {
                Log.d("VersionControl", "App version is up to date")
                Result.UpToDate
            }
        } catch (e: Exception) {
            Log.e("VersionControl", "Error during version check or remote config fetch", e)
            Result.Error(e)
        }
    }


    private fun isOutdated(current: String, required: String): Boolean {
        val currentBase = current.split("-").first()
        val requiredBase = required.split("-").first()
        val currentParts = currentBase.split(".").mapNotNull { it.toIntOrNull() }
        val requiredParts = requiredBase.split(".").mapNotNull { it.toIntOrNull() }

        if (currentParts.isEmpty() || requiredParts.isEmpty()) {
            return currentBase < requiredBase
        }
        for (i in 0 until maxOf(currentParts.size, requiredParts.size)) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val requiredPart = requiredParts.getOrElse(i) { 0 }

            when {
                currentPart < requiredPart -> return true
                currentPart > requiredPart -> return false
            }
        }
        return false
    }

    sealed interface Result {
        data object UpToDate : Result
        data class Outdated(val url: String) : Result
        data class Error(val throwable: Throwable) : Result
    }

    data class MinimumRequiredVersion(
        val version: String,
        val url: String
    )
}
