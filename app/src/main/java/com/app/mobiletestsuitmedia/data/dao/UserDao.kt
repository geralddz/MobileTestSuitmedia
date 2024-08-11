package com.app.mobiletestsuitmedia.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.mobiletestsuitmedia.data.response.DataItem

@Dao
interface UserDao {
    @Query("SELECT * FROM User")
    fun getAllUser(): PagingSource<Int, DataItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(stories: List<DataItem>)

    @Query("DELETE FROM User")
    suspend fun clearAll()
}