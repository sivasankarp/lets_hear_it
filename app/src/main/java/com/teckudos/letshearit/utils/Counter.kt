package com.teckudos.letshearit.utils

import android.os.Handler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent

class Counter(lifecycle: Lifecycle) : LifecycleObserver {

    private var handler = Handler()
    private lateinit var runnable: Runnable

    var secondsCount = MutableLiveData<Int>()

    init {
        lifecycle.addObserver(this)
    }

    fun startTimer() {
        secondsCount.value = 0
        runnable = Runnable {
            secondsCount.value = secondsCount.value?.inc()
            print("logs" + secondsCount.value)
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopTimer() {
        if (::runnable.isInitialized) {
            handler.removeCallbacks(runnable)
        }
    }

}