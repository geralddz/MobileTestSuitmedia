package com.app.mobiletestsuitmedia.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.mobiletestsuitmedia.data.repository.UserRepository
import com.app.mobiletestsuitmedia.di.Injection
import com.app.mobiletestsuitmedia.ui.viewmodel.FirstScreenViewModel
import com.app.mobiletestsuitmedia.ui.viewmodel.SecondScreenViewModel
import com.app.mobiletestsuitmedia.ui.viewmodel.ThirdScreenViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FirstScreenViewModel::class.java) -> {
                FirstScreenViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SecondScreenViewModel::class.java) -> {
                SecondScreenViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ThirdScreenViewModel::class.java) -> {
                ThirdScreenViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}