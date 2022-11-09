package android.example.homescout.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject



class TrackingPreferencesRepository @Inject constructor(private val dataStore: DataStore<Preferences>){

    private object PreferenceKeys {
        val IS_TRACKING_ENABLED = booleanPreferencesKey("is_tracking_enabled")
        val DISTANCE = floatPreferencesKey("distance")
        val TIME = floatPreferencesKey("time")
        val OCCURRENCES = floatPreferencesKey("occurrences")
    }


    val isTrackingEnabled = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.IS_TRACKING_ENABLED] ?: false
    }

    val distance = dataStore.data.map { preferences ->
        // Timber.i( "distance: ${preferences[PreferenceKeys.DISTANCE]}")
        preferences[PreferenceKeys.DISTANCE] ?: 250.0f
    }

    val time = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.TIME] ?: 35.0f
    }

    val occurrences = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.OCCURRENCES] ?: 5.0f
    }

    suspend fun updateIsTrackingEnabled(isTrackingEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.IS_TRACKING_ENABLED] = isTrackingEnabled
        }
    }

    suspend fun updateDistance(distance: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DISTANCE] = distance
        }
    }

    suspend fun updateTime(time: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TIME] = time
        }
    }

    suspend fun updateOccurrences(occurrences: Float) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.OCCURRENCES] = occurrences
        }
    }


}
