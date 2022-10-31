package android.example.homescout

import android.annotation.SuppressLint
import android.bluetooth.le.ScanResult
import android.example.homescout.models.DeviceTypeManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlin.experimental.and

class BluetoothAPILogger() {

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("MissingPermission")
    fun logResults(result: ScanResult) {

        // prepare to identify an device by manufacturer
        val manufacturerData = result.scanRecord?.manufacturerSpecificData
        //val services = result.scanRecord?.serviceUuids
        //Log.i("BluetoothAPILogger - Service", "$services")

        val tmp_device = DeviceTypeManager.identifyDeviceType(result)
        tmp_device.printManufacturer()

        // identification for AirTag
        val statusByte: Byte? = result.scanRecord?.getManufacturerSpecificData(0x004c)?.get(2) // 16 for AirTag
        Log.i("BluetoothAPILogger - StatusByte", "${statusByte}")
        val deviceTypeInt = (statusByte?.and(0x30)?.toInt()?.shr(4)) // type = 1 for AirTag
        Log.i("BluetoothAPILogger - deviceTypeInt", "${deviceTypeInt}")
        val device = result.device
        val address = device.address
        Log.i("BluetoothAPILogger - address", "${address}")

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

        Log.i("BluetoothAPILogger - describeContents", "${describeContents}")
        Log.i("BluetoothAPILogger - advertisingSid", "${advertisingSid}")
        Log.i("BluetoothAPILogger - dataStatus", "${dataStatus}")
        Log.i("BluetoothAPILogger - periodicAdvertisingInterval", "${periodicAdvertisingInterval}")
        Log.i("BluetoothAPILogger - primaryPhy", "${primaryPhy}")
        Log.i("BluetoothAPILogger - rssi", "${rssi}")
        Log.i("BluetoothAPILogger - secondaryPhy", "${secondaryPhy}")
        Log.i("BluetoothAPILogger - timestampNanos", "${timestampNanos}")
        Log.i("BluetoothAPILogger - txPower", "${txPower}")
        Log.i("BluetoothAPILogger - hashCode", "${hashCode}")
        Log.i("BluetoothAPILogger - isConnectable", "${isConnectable}")
        Log.i("BluetoothAPILogger - isLegacy", "${isLegacy}")
        Log.i("BluetoothAPILogger - toString", "${toString}")

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

        //Log.i("BluetoothAPILogger - fetchUuidWithSdp", "${fetchUuidWithSdp}")
        Log.i("BluetoothAPILogger - alias", "${alias}")
        Log.i("BluetoothAPILogger - bluetoothClass", "${bluetoothClass}")
        Log.i("BluetoothAPILogger - bluetoothClassMajor", "${bluetoothClassMajor}")
        Log.i("BluetoothAPILogger - bondState", "${bondState}")
        Log.i("BluetoothAPILogger - deviceMame", "${deviceMame}")
        Log.i("BluetoothAPILogger - type", "${type}")
        Log.i("BluetoothAPILogger - uuids", "${uuids}")
        Log.i("BluetoothAPILogger - deviceToString", "${deviceToString}")

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
//                Log.i("BluetoothAPILogger - size MFD", "size: $size")
//                Log.i("BluetoothAPILogger - iterate MFD", "key: $key value: $value")
//                value?.forEach { byte ->  Log.i("BluetoothAPILogger - iterate ByteArray", "byte: $byte") }
//            }

        Log.i("BluetoothAPILogger - SRadvertisingFlags", "${SRadvertisingFlags}")
        Log.i("BluetoothAPILogger - bytes", "${bytes}")
        Log.i("BluetoothAPILogger - SRdeviceName", "${SRdeviceName}")
        Log.i("BluetoothAPILogger - manufacturerSpecificData", "${manufacturerSpecificData}")
        Log.i("BluetoothAPILogger - serviceData", "${serviceData}")
        Log.i("BluetoothAPILogger - serviceSolicitationUuids", "${serviceSolicitationUuids}")
        Log.i("BluetoothAPILogger - serviceUuids", "${serviceUuids}")
        Log.i("BluetoothAPILogger - SRtxPowerLevel", "${SRtxPowerLevel}")
        Log.i("BluetoothAPILogger - SRtoString", "${SRtoString}")

//            val appleManufacturerSpecificData = result.scanRecord?.getManufacturerSpecificData(0x004c)
//
//            if (appleManufacturerSpecificData != null && deviceTypeInt == 1) {
//                Log.i("BluetoothAPILogger", "- INVESTIGATING AIRTAG")
//                val statusByte: Byte = appleManufacturerSpecificData[2]
//                Log.i("BluetoothAPILogger - statusByte", "${statusByte}")
//
//                val statusByteAnd = statusByte.and(0x30).toString(radix = 16)
//                Log.i("BluetoothAPILogger - statusByteAnd", "${statusByteAnd}")
//
//                val statusByteAndtoInt = statusByte.and(0x30).toInt().toString(radix = 2)
//                Log.i("BluetoothAPILogger - statusByteAndtoInt", "${statusByteAndtoInt}")
//
//                // Get the correct int from the byte
//                val deviceTypeInt = (statusByte.and(0x30).toInt() shr 4).toString(radix = 16)
//                Log.i("BluetoothAPILogger - deviceTypeInt", "${deviceTypeInt}")
//
//            }


    }
}