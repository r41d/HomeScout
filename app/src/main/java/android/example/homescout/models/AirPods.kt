package android.example.homescout.models

import android.util.Log

class AirPods : DeviceType {

    override val type = "AirPods"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "AirPods")
    }
}
