package android.example.homescout.repositories

import android.example.homescout.database.BLEDevice
import android.example.homescout.database.BLEDeviceDao
import android.example.homescout.database.UserPosition
import android.example.homescout.database.UserPositionDao
import javax.inject.Inject


class MainRepository @Inject constructor(
    val bleDeviceDao: BLEDeviceDao,
    val userPositionDao: UserPositionDao
) {

    suspend fun insertBLEDevice(bleDevice: BLEDevice) = bleDeviceDao.insertBLEDevice(bleDevice)
    suspend fun deleteBLEDevice(bleDevice: BLEDevice) = bleDeviceDao.deleteBLEDevice(bleDevice)
    suspend fun deleteBLEDevicesOlderThanOneWeek() = bleDeviceDao.deleteBLEDevicesOlderThanOneWeek()
    fun getAllBLEDevicesSortedByDate() = bleDeviceDao.getAllBLEDevicesSortedByDate()

    suspend fun insertUserPosition(userPosition: UserPosition) = userPositionDao.insertUserPosition(userPosition)
    suspend fun deleteUserPosition(userPosition: UserPosition) = userPositionDao.deleteUserPosition(userPosition)
    suspend fun deleteUserPositionOlderThanOneHour() = userPositionDao.deleteUserPositionOlderThanOneHour()
    fun getAllUserPositionsSortedByDate() = userPositionDao.getAllUserPositionsSortedByDate()


}