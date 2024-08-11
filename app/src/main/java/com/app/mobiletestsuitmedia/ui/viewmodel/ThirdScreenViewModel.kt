package com.app.mobiletestsuitmedia.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.mobiletestsuitmedia.data.repository.UserRepository
import com.app.mobiletestsuitmedia.data.response.DataItem
import kotlinx.coroutines.launch

class ThirdScreenViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorText = MutableLiveData<String?>()
    val errorText: LiveData<String?> = _errorText

    private fun setErrorText(errorMessage: String?) {
        _errorText.value = errorMessage
    }

    fun saveUsername(username: String) {
        viewModelScope.launch {
            repository.saveUsernames(username)
        }
    }

    val isEmpty: LiveData<Boolean> = repository.isEmpty
    val user: LiveData<PagingData<DataItem>> =
        repository.getUserAll().cachedIn(viewModelScope)

}