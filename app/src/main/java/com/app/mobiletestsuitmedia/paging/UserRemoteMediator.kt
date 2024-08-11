
package com.app.mobiletestsuitmedia.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.app.mobiletestsuitmedia.data.database.UserDatabase
import com.app.mobiletestsuitmedia.data.model.RemoteKeys
import com.app.mobiletestsuitmedia.data.response.DataItem
import com.app.mobiletestsuitmedia.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(
    private val userDatabase: UserDatabase,
    private val apiService: ApiService,
    private val onEmptyState: (Boolean) -> Unit
) : RemoteMediator<Int, DataItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DataItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            Log.d("StoryRemoteMediator", "Loading page: $page with pageSize: ${state.config.pageSize}")
            val responseData = apiService.getUsers(page, state.config.pageSize).data

            val endOfPaginationReached = responseData.isEmpty()

            Log.d("StoryRemoteMediator", "Received response data: $responseData")

            userDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    userDatabase.remoteKeysDao().deleteRemoteKeys()
                    userDatabase.userDao().clearAll()
                    onEmptyState(endOfPaginationReached)
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseData.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                userDatabase.remoteKeysDao().insertAll(keys)
                userDatabase.userDao().insertAll(responseData)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            Log.e("StoryRemoteMediator", "Error loading data", exception)
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, DataItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            userDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, DataItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            userDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, DataItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                userDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}