package com.teckudos.letshearit.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SoundPlayViewModel : ViewModel() {

    val audioPlaying = MutableLiveData<Boolean?>()

    fun playAudio() {
        audioPlaying.value = true
    }
}