package android.example.homescout.models

import android.util.SparseArray




class DeviceFactory {

    companion object {
        fun createDevice(companyIdentifier: SparseArray<ByteArray>?): Device {
            if (companyIdentifier?.get(APPLE) != null) return Apple()
            if (companyIdentifier?.get(SAMSUNG) != null) return Samsung()
            if (companyIdentifier?.get(TILE) != null) return Tile()
            if (companyIdentifier?.get(CHIPOLO) != null) return Chipolo()
            else return Unknown()
        }

        private const val APPLE = 76
        private const val SAMSUNG = 117
        private const val TILE = 1660
        private const val CHIPOLO = 2243
    }
}