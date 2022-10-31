package android.example.homescout.models

import android.util.Log

class AirTag : DeviceType {

    override val type = "AirTag"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "AirTag")
    }
}
