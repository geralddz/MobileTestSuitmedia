package com.app.mobiletestsuitmedia.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mobiletestsuitmedia.data.repository.UserRepository
import kotlinx.coroutines.launch

class SecondScreenViewModel(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData("")
    val users: MutableLiveData<String> = _users

    private val _usersname = MutableLiveData("")
    val usersname: MutableLiveData<String> = _usersname

    init {
        viewModelScope.launch {
            repository.getUser().collect {
                _users.value = it
            }
        }

        viewModelScope.launch {
            repository.getUsername().collect{
                _usersname.value = it
            }
        }
    }

    fun clearAll(){
        viewModelScope.launch {
            repository.clearUsers()
        }
    }
}