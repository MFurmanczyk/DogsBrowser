package com.example.dogsbrowser.view

import android.view.View

interface DogClickListener {
    fun onDogClicked(v: View, dogUuid: Int)
}