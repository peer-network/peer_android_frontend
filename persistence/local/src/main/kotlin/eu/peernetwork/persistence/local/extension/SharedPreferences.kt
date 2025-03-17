package eu.peernetwork.persistence.local.extension

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun<T> SharedPreferences.retrieve(
    key: String,
    block: (String) -> T
): T? {
    return if (contains(key)) {
        block(key)
    } else {
        null
    }
}

fun<T> SharedPreferences.publishOn(
    key: String,
    block: SharedPreferences.Editor.(String) -> T
) {
    edit().run {
        block(key)
        commit()
    }
}

fun<T> SharedPreferences.observeOn(
    key: String,
    block: (String) -> T
): Flow<T?> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, current ->
        if (current == key) {
            trySend(block(key))
        }
    }
    if (contains(key)) {
        trySend(block(key))
    } else {
        trySend(null)
    }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose {
        unregisterOnSharedPreferenceChangeListener(listener)
    }
}
