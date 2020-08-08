package com.teckudos.letshearit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teckudos.letshearit.utils.Counter

class MainViewModelProviderFactory(private val counter: Counter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(counter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}