package android.example.homescout.repositories

import android.example.homescout.database.BLEDevice
import android.example.homescout.database.BLEDeviceDao
import android.example.homescout.database.UserPosition
import android.example.homescout.database.UserPositionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import javax.inject.Inject


class MainRepository @Inject constructor(
    private val bleDeviceDao: BLEDeviceDao,
    private val userPositionDao: UserPositionDao
) {

    suspend fun insertBLEDevice(bleDevice: BLEDevice) {
        withContext(Dispatchers.IO + NonCancellable) {
            bleDeviceDao.insertBLEDevice(bleDevice)
        }
    }

    suspend fun deleteBLEDevice(bleDevice: BLEDevice) = bleDeviceDao.deleteBLEDevice(bleDevice)

    suspend fun deleteBLEDevicesOlderThanOneHour() {
        withContext(Dispatchers.IO + NonCancellable) {
            bleDeviceDao.deleteBLEDevicesOlderThanOneHour()
        }
    }
    fun getAllBLEDevicesSortedByDate() = bleDeviceDao.getAllBLEDevicesSortedByDate()

    suspend fun insertUserPosition(userPosition: UserPosition) = userPositionDao.insertUserPosition(userPosition)
    suspend fun deleteUserPosition(userPosition: UserPosition) = userPositionDao.deleteUserPosition(userPosition)
    suspend fun deleteUserPositionOlderThanOneHour() = userPositionDao.deleteUserPositionOlderThanOneHour()
    fun getAllUserPositionsSortedByDate() = userPositionDao.getAllUserPositionsSortedByDate()


}