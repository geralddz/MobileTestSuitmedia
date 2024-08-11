package com.app.mobiletestsuitmedia.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.app.mobiletestsuitmedia.data.database.UserDatabase
import com.app.mobiletestsuitmedia.data.datastore.UserPreference
import com.app.mobiletestsuitmedia.data.response.DataItem
import com.app.mobiletestsuitmedia.data.retrofit.ApiService
import com.app.mobiletestsuitmedia.paging.UserRemoteMediator

class UserRepository(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val userDatabase: UserDatabase
) {
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> get() = _isEmpty

    @OptIn(ExperimentalPagingApi::class)
    fun getUserAll(): LiveData<PagingData<DataItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 4
            ),
            remoteMediator = UserRemoteMediator(userDatabase, apiService){
                isEmpty -> _isEmpty.postValue(isEmpty)
            },
            pagingSourceFactory = {
                userDatabase.userDao().getAllUser()
            }
        ).liveData
    }

    suspend fun saveUsers(name: String) {
        userPreference.saveUser(name)
    }

    suspend fun saveUsernames(username: String) {
        userPreference.saveUsername(username)
    }

    fun getUser() = userPreference.getSaveUser()

    fun getUsername() = userPreference.getSaveUsername()

    suspend fun clearUsers(){
        userPreference.clearUser()
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference, userDatabase: UserDatabase) = UserRepository(apiService, userPreference, userDatabase)
    }

}