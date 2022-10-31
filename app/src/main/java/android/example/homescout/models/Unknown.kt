package android.example.homescout.models

import android.util.Log

class Unknown : DeviceType {

    override val type = "Unknown"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "Unknown")
    }
}
