package com.app.mobiletestsuitmedia.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.mobiletestsuitmedia.data.dao.RemoteKeysDao
import com.app.mobiletestsuitmedia.data.dao.UserDao
import com.app.mobiletestsuitmedia.data.model.RemoteKeys
import com.app.mobiletestsuitmedia.data.response.DataItem

@Database(entities = [DataItem::class, RemoteKeys::class], version = 1, exportSchema = false)

abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDB(context: Context): UserDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "UserDB"
                ).build().also { INSTANCE = it }
            }
    }
}