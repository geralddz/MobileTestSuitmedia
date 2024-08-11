package com.app.mobiletestsuitmedia.di

import android.content.Context
import com.app.mobiletestsuitmedia.data.database.UserDatabase
import com.app.mobiletestsuitmedia.data.datastore.UserPreference
import com.app.mobiletestsuitmedia.data.repository.UserRepository
import com.app.mobiletestsuitmedia.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val userPreference = UserPreference.getInstance(context)
        val apiService = ApiConfig.getApiService()
        val userDatabase = UserDatabase.getDB(context)
        return UserRepository(apiService, userPreference, userDatabase)
    }
}