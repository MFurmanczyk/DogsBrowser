package com.example.dogsbrowser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.model.DogsApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel: ViewModel() {

    private val dogsService = DogsApiService()
    private val disposable = CompositeDisposable()

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
        fetchFromRemote()
    }

    private  fun fetchFromRemote() {
        _loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        _dogs.value = t
                        _loading.value = false
                        _dogsLoadError.value = false
                    }

                    override fun onError(e: Throwable) {
                        _loading.value = false
                        _dogsLoadError.value = true
                        e.printStackTrace()
                    }

                })
        )

    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}