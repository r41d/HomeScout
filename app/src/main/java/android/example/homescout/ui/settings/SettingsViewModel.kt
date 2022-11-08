package android.example.homescout.ui.settings

import android.util.Log
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val trackingPreferencesRepository : TrackingPreferencesRepository
): ViewModel() {

    private val _isSwitchEnabled = MutableLiveData<Boolean>()
    val isSwitchEnabled: LiveData<Boolean>
        get() = trackingPreferencesRepository.isTrackingEnabled.asLiveData()

    private val _distance = MutableLiveData<Float>()
    val distance: LiveData<Float>
        get() = trackingPreferencesRepository.distance.asLiveData()

    private val _time = MutableLiveData<Float>()
    val time: LiveData<Float>
        get() = trackingPreferencesRepository.time.asLiveData()

    private val _occurrences = MutableLiveData<Float>()
    val occurrences: LiveData<Float>
        get() = trackingPreferencesRepository.occurrences.asLiveData()


    fun onSwitchToggled(checked: Boolean) {
        viewModelScope.launch {
            trackingPreferencesRepository.updateIsTrackingEnabled(checked)
        }
    }

    fun updateDistance(value: Float) {
        viewModelScope.launch {
            trackingPreferencesRepository.updateDistance(value)
        }
    }

    fun updateTime(value: Float) {
        viewModelScope.launch {
            trackingPreferencesRepository.updateTime(value)
        }
    }

    fun updateOccurrences(value: Float) {
        viewModelScope.launch {
            trackingPreferencesRepository.updateOccurrences(value)
        }
    }

}