package android.example.homescout

import android.bluetooth.le.ScanResult
import android.example.homescout.models.DeviceFactory
import android.util.Log
import kotlin.experimental.and

class BluetoothAPILogger() {


    fun logResults(result: ScanResult) {

        // prepare to identify an device by manufacturer
        val manufacturerData = result.scanRecord?.manufacturerSpecificData
        //val services = result.scanRecord?.serviceUuids
        //Log.i("DeviceType - Service", "$services")

        val tmp_device = DeviceFactory.createDevice(manufacturerData)
        tmp_device.printManufacturer()

        // identification for AirTag
        val statusByte: Byte? = result.scanRecord?.getManufacturerSpecificData(0x004c)?.get(2) // 16 for AirTag
        Log.i("DeviceType - StatusByte", "${statusByte}")
        val deviceTypeInt = (statusByte?.and(0x30)?.toInt()?.shr(4)) // type = 1 for AirTag
        Log.i("DeviceType - deviceTypeInt", "${deviceTypeInt}")
        val device = result.device
        val address = device.address
        Log.i("DeviceType - address", "${address}")

        // methods or properties from scanResult
        val describeContents = result.describeContents()
        val advertisingSid = result.advertisingSid
        val dataStatus = result.dataStatus
        // device already declared
        val periodicAdvertisingInterval = result.periodicAdvertisingInterval
        val primaryPhy = result.primaryPhy
        val rssi = result.rssi
        val scanRecord = result.scanRecord
        val secondaryPhy = result.secondaryPhy
        val timestampNanos = result.timestampNanos
        val txPower = result.txPower
        val hashCode = result.hashCode().toString()
        val isConnectable = result.isConnectable
        val isLegacy = result.isLegacy
        val toString = result.toString()

        Log.i("DeviceType - describeContents", "${describeContents}")
        Log.i("DeviceType - advertisingSid", "${advertisingSid}")
        Log.i("DeviceType - dataStatus", "${dataStatus}")
        Log.i("DeviceType - periodicAdvertisingInterval", "${periodicAdvertisingInterval}")
        Log.i("DeviceType - primaryPhy", "${primaryPhy}")
        Log.i("DeviceType - rssi", "${rssi}")
        Log.i("DeviceType - secondaryPhy", "${secondaryPhy}")
        Log.i("DeviceType - timestampNanos", "${timestampNanos}")
        Log.i("DeviceType - txPower", "${txPower}")
        Log.i("DeviceType - hashCode", "${hashCode}")
        Log.i("DeviceType - isConnectable", "${isConnectable}")
        Log.i("DeviceType - isLegacy", "${isLegacy}")
        Log.i("DeviceType - toString", "${toString}")

        // methods or properties from BluetoothDevice
        //val fetchUuidWithSdp = device.fetchUuidsWithSdp().toString()
        // address already in place
        val alias = device.alias
        val bluetoothClass = device.bluetoothClass.deviceClass
        val bluetoothClassMajor = device.bluetoothClass.majorDeviceClass
        val bondState = device.bondState
        val deviceMame = device.name
        val type = device.type
        val uuids = device.uuids
        val deviceToString = device.toString()

        //Log.i("DeviceType - fetchUuidWithSdp", "${fetchUuidWithSdp}")
        Log.i("DeviceType - alias", "${alias}")
        Log.i("DeviceType - bluetoothClass", "${bluetoothClass}")
        Log.i("DeviceType - bluetoothClassMajor", "${bluetoothClassMajor}")
        Log.i("DeviceType - bondState", "${bondState}")
        Log.i("DeviceType - deviceMame", "${deviceMame}")
        Log.i("DeviceType - type", "${type}")
        Log.i("DeviceType - uuids", "${uuids}")
        Log.i("DeviceType - deviceToString", "${deviceToString}")

        // methods or properties from ScanRecord
        val SRadvertisingFlags = scanRecord?.advertiseFlags
        val bytes = scanRecord?.bytes
        val SRdeviceName = scanRecord?.deviceName
        val manufacturerSpecificData = scanRecord?.manufacturerSpecificData
        val serviceData = scanRecord?.serviceData
        val serviceSolicitationUuids = scanRecord?.serviceSolicitationUuids
        val serviceUuids = scanRecord?.serviceUuids
        val SRtxPowerLevel = scanRecord?.txPowerLevel
        val SRtoString = scanRecord?.toString()


        // iterate over manufacturer data
//            val size = manufacturerSpecificData?.size()
//            for (i in 0 until size!!) {
//                val key: Int = manufacturerSpecificData.keyAt(i)
//                val value: ByteArray? = manufacturerSpecificData.valueAt(i)
//                Log.i("DeviceType - size MFD", "size: $size")
//                Log.i("DeviceType - iterate MFD", "key: $key value: $value")
//                value?.forEach { byte ->  Log.i("DeviceType - iterate ByteArray", "byte: $byte") }
//            }

        Log.i("DeviceType - SRadvertisingFlags", "${SRadvertisingFlags}")
        Log.i("DeviceType - bytes", "${bytes}")
        Log.i("DeviceType - SRdeviceName", "${SRdeviceName}")
        Log.i("DeviceType - manufacturerSpecificData", "${manufacturerSpecificData}")
        Log.i("DeviceType - serviceData", "${serviceData}")
        Log.i("DeviceType - serviceSolicitationUuids", "${serviceSolicitationUuids}")
        Log.i("DeviceType - serviceUuids", "${serviceUuids}")
        Log.i("DeviceType - SRtxPowerLevel", "${SRtxPowerLevel}")
        Log.i("DeviceType - SRtoString", "${SRtoString}")

//            val appleManufacturerSpecificData = result.scanRecord?.getManufacturerSpecificData(0x004c)
//
//            if (appleManufacturerSpecificData != null && deviceTypeInt == 1) {
//                Log.i("DeviceType", "- INVESTIGATING AIRTAG")
//                val statusByte: Byte = appleManufacturerSpecificData[2]
//                Log.i("DeviceType - statusByte", "${statusByte}")
//
//                val statusByteAnd = statusByte.and(0x30).toString(radix = 16)
//                Log.i("DeviceType - statusByteAnd", "${statusByteAnd}")
//
//                val statusByteAndtoInt = statusByte.and(0x30).toInt().toString(radix = 2)
//                Log.i("DeviceType - statusByteAndtoInt", "${statusByteAndtoInt}")
//
//                // Get the correct int from the byte
//                val deviceTypeInt = (statusByte.and(0x30).toInt() shr 4).toString(radix = 16)
//                Log.i("DeviceType - deviceTypeInt", "${deviceTypeInt}")
//
//            }


    }
}