package maxeem.america.devbytes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface Dao {

    @Query("SELECT rowid, * FROM videos ORDER BY datetime(updated) DESC")
    fun getAll(): LiveData<List<DatabaseVideoQuery>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(vararg videos: DatabaseVideo) : List<Long>

}
