/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package maxeem.america.devbyteviewer.database

import androidx.lifecycle.LiveData
import androidx.room.*
import maxeem.america.devbyteviewer.app
import maxeem.america.devbyteviewer.util.DATABASE_NAME

@Dao
interface VideosDao {

    @Query("SELECT * FROM videos")
    fun getAll(): LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseVideo)

}

@Database(entities = [DatabaseVideo::class], version = 1)
abstract class VideosDatabase : RoomDatabase() {

    abstract val videosDao : VideosDao

    companion object {
        val instance by lazy {
            Room.databaseBuilder(app, VideosDatabase::class.java, DATABASE_NAME).run {
                fallbackToDestructiveMigration()
                build()
            }
        }
    }

}
