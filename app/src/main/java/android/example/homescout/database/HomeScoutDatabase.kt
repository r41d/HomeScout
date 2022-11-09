package android.example.homescout.database

import androidx.room.Database
import androidx.room.TypeConverters

@Database(
    entities = [BLEDevice::class, UserPosition::class],
    version = 1)
//@TypeConverters(Converter::class)
abstract class HomeScoutDatabase {

    abstract fun getBLEDeviceDao(): BLEDeviceDao
    abstract fun getUserPositionDao(): UserPositionDao
}