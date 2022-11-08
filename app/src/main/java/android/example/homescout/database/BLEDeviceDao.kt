package android.example.homescout.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BLEDeviceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBLEDevice(bleDevice: BLEDevice)

    @Query("SELECT * FROM ble_device_table ORDER BY timestampInMilliSeconds DESC")
    fun getAllBLEDevicesSortedByDate() : LiveData<List<BLEDevice>>

    @Delete
    suspend fun deleteBLEDevice(bleDevice: BLEDevice)

    @Query("DELETE FROM ble_device_table WHERE timestampInMilliSeconds <= (strftime('%s','now', '-7 day') * 1000)")
    suspend fun deleteBLEDevicesOlderThanOneWeek()


}