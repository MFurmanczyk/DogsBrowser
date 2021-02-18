package com.example.dogsbrowser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogsbrowser.model.DogBreed

class DetailsViewModel: ViewModel() {

    private val _dog = MutableLiveData<DogBreed>()
    val dog: LiveData<DogBreed> get() = _dog

    fun setDog() {
        val dog = DogBreed("1", "testTESTtest", "TestowyTest", "Test", "Test", "Test", "Test")
        _dog.value = dog
    }

}