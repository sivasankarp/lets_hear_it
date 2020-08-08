package com.teckudos.letshearit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teckudos.letshearit.utils.Counter

class RecordViewModelProviderFactory(private val counter: Counter) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SoundRecordViewModel::class.java)) {
            return SoundRecordViewModel(counter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}