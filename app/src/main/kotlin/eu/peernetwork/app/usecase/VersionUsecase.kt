package eu.peernetwork.app.usecase

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.interactor.RemoteInteractor
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VersionUseCase @Inject constructor(
    private val interactor: RemoteInteractor,
    private val remoteConfig: FirebaseRemoteConfig,
    private val dispatcher: Dispatcher
) : SuspendableUseCase<VersionUseCase.Result> {

    override suspend fun invoke(): Result = withContext(dispatcher.io) {
        try {
            remoteConfig.fetchAndActivate().await()
            val json = remoteConfig.getString("minimum_required_version")
            Log.d("VersionControl", "Fetched minimum_required_version JSON: $json")
            val listType = object : TypeToken<List<MinimumRequiredVersion>>() {}.type
            val versionList: List<MinimumRequiredVersion> = Gson().fromJson(json, listType)
            val currentVersion = BuildConfig.VERSION_NAME
            Log.d("VersionControl", "Current app version: $currentVersion")
            val matchedVersion = versionList.firstOrNull { it.version == currentVersion }
            val minimumVersion = matchedVersion ?: run {
                val isDebug = currentVersion.contains("-DEBUG")
                val fallbackList = versionList.filter { it.version.contains("-DEBUG") == isDebug }
                fallbackList.maxByOrNull { it.version.split("-").first() }
            }
            Log.d("VersionControl", "Matched version: ${minimumVersion?.version}")
            Log.d("VersionControl", "Matched URL: ${minimumVersion?.url}")
            minimumVersion?.url?.let {
                Log.d("VersionControl", "Updating provider baseUrl to: $it")
                interactor.set(it)
            } ?: Log.d("VersionControl", "No base URL found in matched version")
            if (minimumVersion != null && isOutdated(minimumVersion.version)) {
                Log.d("VersionControl", "App version is outdated")
                  Result.Outdated(BuildConfig.PLAYSTORE_URL)
            } else {
                Log.d("VersionControl", "App version is up to date")
                Result.UpToDate
            }
        } catch (e: Exception) {
            Log.e("VersionControl", "Error during version check or remote config fetch", e)
            Result.Error(e)
        }
    }

    private fun isOutdated(required: String): Boolean {
        val currentBase = BuildConfig.VERSION_NAME.split("-").first()
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
