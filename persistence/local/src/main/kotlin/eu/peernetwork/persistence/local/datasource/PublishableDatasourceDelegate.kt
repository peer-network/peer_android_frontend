package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import eu.peernetwork.persistence.data.datasource.PublishableDatasource
import eu.peernetwork.persistence.local.extension.publishOn
import javax.inject.Inject

class PublishableDatasourceDelegate @Inject constructor(
    private val preference: SharedPreferences
) : PublishableDatasource {
    override suspend fun putLong(key: String, value: Long) {
        preference.publishOn(key) {
            putLong(it, value)
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        preference.publishOn(key) {
            putBoolean(it, value)
        }
    }

    override suspend fun putInteger(key: String, value: Int) {
        preference.publishOn(key) {
            putInt(it, value)
        }
    }

    override suspend fun putString(key: String, value: String) {
        preference.publishOn(key) {
            putString(it, value)
        }
    }

    override suspend fun remove(key: String) {
        preference.publishOn(key) {
            remove(it)
        }
    }
}
