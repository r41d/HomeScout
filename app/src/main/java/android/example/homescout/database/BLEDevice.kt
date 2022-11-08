package android.example.homescout.database

import android.example.homescout.models.DeviceType
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng
import java.sql.Timestamp

@Entity(tableName = "ble_device_table")
data class BLEDevice(
    var macAddress: String? = null,
    var timestampInMilliSeconds: Long = 0L,
    var location: LatLng,
    var type: DeviceType
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}