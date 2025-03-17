package eu.peernetwork.persistence.local.datasource

import android.content.SharedPreferences
import eu.peernetwork.persistence.data.datasource.RetrievableDatasource
import eu.peernetwork.persistence.local.extension.retrieve
import javax.inject.Inject

class RetrievableDatasourceDelegate @Inject constructor(
    private val preference: SharedPreferences
) : RetrievableDatasource {
    override fun getLong(key: String): Long? {
        return preference.retrieve(key) {
            preference.getLong(it, 0)
        }
    }

    override fun getBoolean(key: String): Boolean? {
        return preference.retrieve(key) {
            preference.getBoolean(it, false)
        }
    }

    override fun getInteger(key: String): Int? {
        return preference.retrieve(key) {
            preference.getInt(it, 0)
        }
    }

    override fun getString(key: String): String? {
        return preference.retrieve(key) {
            preference.getString(it, null)
        }
    }
}
