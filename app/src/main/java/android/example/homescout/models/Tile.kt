package android.example.homescout.models

import android.util.Log

class Tile : DeviceType {

    override val type = "Tile"

    override fun printManufacturer() {
        Log.i("BluetoothAPILogger", "Tile")
    }
}
