package android.example.homescout.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is welcome Fragment"
    }
    val text: LiveData<String> = _text
}