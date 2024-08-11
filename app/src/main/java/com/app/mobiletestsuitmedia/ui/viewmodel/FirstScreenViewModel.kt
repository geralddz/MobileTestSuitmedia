package com.app.mobiletestsuitmedia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mobiletestsuitmedia.data.repository.UserRepository
import kotlinx.coroutines.launch

class FirstScreenViewModel(private val repository: UserRepository) : ViewModel() {

    fun saveUser(name: String) {
        viewModelScope.launch {
            repository.saveUsers(name)
        }
    }

}