package android.example.homescout.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_position_table")
data class UserPosition(
    var lat: Double,
    var lng: Double,
    var timestampInMilliSeconds: Long = 0L
) {
    @PrimaryKey
    var id: Int? = null
}