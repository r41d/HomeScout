package android.example.homescout.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar

class SettingsViewModel : ViewModel() {

    private val _isSwitchEnabled = MutableLiveData<Boolean>(false)
    val isSwitchEnabled: LiveData<Boolean>
        get() = _isSwitchEnabled

    fun onSwitchToggled() {
        (!isSwitchEnabled.value!!).also { _isSwitchEnabled.value = it }
    }

}