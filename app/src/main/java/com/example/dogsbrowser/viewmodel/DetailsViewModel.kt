package com.example.dogsbrowser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.model.DogDatabase
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application): BaseViewModel(application) {

    private val _dog = MutableLiveData<DogBreed>()
    val dog: LiveData<DogBreed> get() = _dog

    fun setDog(uuid: Int) {
        launch {
            _dog.value = DogDatabase(getApplication()).dogDao().getDog(uuid)
        }

    }

}