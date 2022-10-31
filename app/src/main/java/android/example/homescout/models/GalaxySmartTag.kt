package android.example.homescout.models

import android.util.Log

class GalaxySmartTag : DeviceType {

    override val type = "Galaxy SmartTag+"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "GalaxySmartTag")
    }

}
