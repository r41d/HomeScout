package android.example.homescout.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "user_position_table")
data class UserPosition(
    var lat: Double,
    var lng: Double,
    var timestampInMilliSeconds: Long = 0L
) {
    @PrimaryKey
    var id: Int? = null
}