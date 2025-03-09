package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import eu.peernetwork.persistence.data.datasource.ObservableDatasource
import eu.peernetwork.persistence.local.extension.observeOn
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservableDatasourceDelegate @Inject constructor(
    private val preference: SharedPreferences
) : ObservableDatasource {
    override fun observeLong(key: String): Flow<Long?> {
        return preference.observeOn(key) {
            preference.getLong(key, 0L)
        }
    }

    override fun observeBoolean(key: String): Flow<Boolean?> {
        return preference.observeOn(key) {
            preference.getBoolean(key, false)
        }
    }

    override fun observeInteger(key: String): Flow<Int?> {
        return preference.observeOn(key) {
            preference.getInt(key, 0)
        }
    }

    override fun observeString(key: String): Flow<String?> {
        return preference.observeOn(key) {
            preference.getString(key, null)
        }
    }
}
