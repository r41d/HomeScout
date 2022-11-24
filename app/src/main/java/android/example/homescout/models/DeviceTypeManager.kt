package android.example.homescout.models

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.example.homescout.utils.Constants.APPLE_COMPANY_IDENTIFIER
import android.os.ParcelUuid
import kotlin.experimental.and


class DeviceTypeManager {

    companion object {
        @SuppressLint("MissingPermission")
        fun identifyDeviceType(result: ScanResult): DeviceType {

            // Identification of Samsung Galaxy SmartTAG
            if (result.device.name == "Smart Tag") return GalaxySmartTag()

            // Identification of AirTag and Chipolo ONE Spot
            val appleManufacturerSpecificData = result.scanRecord?.manufacturerSpecificData?.get(APPLE_COMPANY_IDENTIFIER)
            if (appleManufacturerSpecificData != null) {

                // according to AirGuard https://dl.acm.org/doi/pdf/10.1145/3507657.3528546
                val statusByte: Byte = appleManufacturerSpecificData[2]
                val deviceTypeInt = (statusByte.and(0x30).toInt() shr 4)
                when (deviceTypeInt) {
                    0 -> return AppleDevice()
                    1 -> return AirTag()
                    2 -> return Chipolo()
                    3 -> return AirPods()
                }
            }

            // Identification of Tile
            val serviceUUID = result.scanRecord?.serviceUuids
            if (serviceUUID != null) {
                val containsTileService = serviceUUID.contains(
                    ParcelUuid.fromString("0000FEED-0000-1000-8000-00805F9B34FB"))
                if (containsTileService) return Tile()
            }

            if (result.device.name != null) {
                return DeviceWithName(result.device.name)
            }

            // Could not identify the device
            return Unknown()
        }
    }
}