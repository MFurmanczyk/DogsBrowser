package com.example.dogsbrowser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogsbrowser.model.DogBreed

class ListViewModel: ViewModel() {

    private val _dogs = MutableLiveData<List<DogBreed>>()
    val dogs: LiveData<List<DogBreed>>
        get() = _dogs

    private val _dogsLoadError = MutableLiveData<Boolean>()
    val dogsLoadError: LiveData<Boolean>
        get() = _dogsLoadError

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun refresh() {
        val dog = DogBreed("1", "test", "Test", "Test", "Test", "Test", "Test")
        val dogList = arrayListOf(dog)
        _dogs.value = dogList
        _dogsLoadError.value = false
        _loading.value = false
    }

}