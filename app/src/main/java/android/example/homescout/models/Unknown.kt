package android.example.homescout.models

import android.util.Log

class Unknown : Device {

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "Unknown")
    }
}
