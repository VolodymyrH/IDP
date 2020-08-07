package com.vholodynskyi.speedometer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private val tag = "MainViewModel"

    val timeStream = MutableLiveData<Unit>()

    fun clickStart() {
        Log.d(tag, "start")

        timeStream.postValue(Unit)
    }

    fun clickEnd() {
        Log.d(tag, "end")
        viewModelScope.cancel()
    }
}