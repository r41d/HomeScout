package android.example.homescout.models

import android.util.Log

class Chipolo : DeviceType {

    override val type = "Chipolo ONE Spot"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "Chipolo")
    }
}
