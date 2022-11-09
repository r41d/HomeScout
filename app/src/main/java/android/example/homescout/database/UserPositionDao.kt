package android.example.homescout.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserPositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserPosition(userPosition: UserPosition)

    @Query("SELECT * FROM user_position_table ORDER BY timestampInMilliSeconds DESC")
    fun getAllUserPositionsSortedByDate() : LiveData<List<UserPosition>>

    @Delete
    suspend fun deleteUserPosition(userPosition: UserPosition)

    @Query("DELETE FROM user_position_table WHERE timestampInMilliSeconds <= (strftime('%s','now', '- 1 hour') * 1000)")
    suspend fun deleteUserPositionOlderThanOneHour()
}