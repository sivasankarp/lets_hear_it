package com.teckudos.letshearit.viewmodels

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import com.teckudos.letshearit.soundtouch.SoundTouch
import com.teckudos.letshearit.utils.Constants
import com.teckudos.letshearit.utils.Counter
import kotlinx.coroutines.*

class MainViewModel(private val counter: Counter) : ViewModel() {

    val showProgress = MutableLiveData<Boolean>().apply { postValue(false) }
    val timerText = MediatorLiveData<String>()
    val processing = MutableLiveData<Boolean>().apply { postValue(false) }

    private val _audioPlaying = MutableLiveData<Boolean?>()
    val audioPlaying: LiveData<Boolean?>
        get() = _audioPlaying

    private val _navigateToPlay = MutableLiveData<Boolean?>()
    val navigateToPlay: LiveData<Boolean?>
        get() = _navigateToPlay

    lateinit var inputFilePath: String
    lateinit var outputFilePath: String
    private lateinit var waveRecorder: WaveRecorder

    private val viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun onStart() {
        showProgress.value = true
        recordSound()
        observeTimer()
        counter.startTimer()
    }

    private fun recordSound() {
        println("inputFilePath$inputFilePath")
        waveRecorder = WaveRecorder(inputFilePath)
        waveRecorder.startRecording()
    }

    private fun observeTimer() {
        timerText.addSource(counter.secondsCount) { timer ->
            if (timer == Constants.MAX) {
                timerText.removeSource(counter.secondsCount)
                counter.stopTimer()
                stopRecording()
            }
            timerText.value = "$timer\nseconds"
        }
    }

    private fun stopRecording() {
        waveRecorder.stopRecording()
        processRecording()
    }

    private fun processRecording() {
        processing.value = true
        viewModelScope.launch {
            var duration = -1.0F
            var res = 0
            withContext(Dispatchers.IO) {
                val soundTouch = SoundTouch()
                // soundTouch.setTempo(1.0F)
                soundTouch.setPitchSemiTones(Constants.PITCH)
                Log.i("SoundTouch", "process file${inputFilePath}")
                val startTime = System.currentTimeMillis()
                res = soundTouch.processFile(inputFilePath, outputFilePath)
                val endTime = System.currentTimeMillis()
                duration = (endTime - startTime) * 0.001f
            }

            Log.i("SoundTouch", "process file done, duration = $duration")
            if (res != 0) {
                val err: String = SoundTouch.getErrorString()
                Log.e("Failure", err)
                timerText.value = null
                counter.secondsCount.value = 0
                processing.value = false
                showProgress.value = false
            } else {
                delay(1000)
                _navigateToPlay.value = true
            }
        }
    }

    fun playAudio() {
        _audioPlaying.value = true
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}