package com.example.myapplication.ui.Joystick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class JoystickViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Joystick Fragment"
    }
    val text: LiveData<String> = _text
}