package android.example.homescout.models

import android.util.Log

class AppleDevice : DeviceType {

    override val type = "AppleDevice"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "AppleDevice")
    }
}
