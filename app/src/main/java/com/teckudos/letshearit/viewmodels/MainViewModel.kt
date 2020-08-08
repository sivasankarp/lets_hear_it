package com.teckudos.letshearit.viewmodels

import android.media.MediaRecorder
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import com.teckudos.letshearit.soundtouch.SoundTouch
import com.teckudos.letshearit.utils.Constants
import com.teckudos.letshearit.utils.Counter
import kotlinx.coroutines.*

class MainViewModel(val counter: Counter) : ViewModel() {

    val showProgress = MutableLiveData<Boolean>().apply { postValue(false) }
    val timerText = MediatorLiveData<String>()

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
            if (timer == 5) {
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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val soundTouch = SoundTouch()
                soundTouch.setTempo(1.0F)
                soundTouch.setPitchSemiTones(Constants.PITCH)
                Log.i("SoundTouch", "process file${inputFilePath}")
                val startTime = System.currentTimeMillis()
                val res: Int = soundTouch.processFile(inputFilePath, outputFilePath)
                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime) * 0.001f

                Log.i("SoundTouch", "process file done, duration = $duration")
                if (res != 0) {
                    val err: String = SoundTouch.getErrorString()
                    Log.e("Failure", err)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}