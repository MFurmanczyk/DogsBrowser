package com.example.dogsbrowser.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dogsbrowser.model.DogBreed
import com.example.dogsbrowser.model.DogDatabase
import com.example.dogsbrowser.model.DogsApiService
import com.example.dogsbrowser.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ListViewModel(application: Application): BaseViewModel(application) {

    private var preferenceHelper = SharedPreferencesHelper(getApplication())
    private val refreshTime = 5 /*min*/ * 60/*sec*/ * 1000/*ms*/ * 1000/*μs*/ * 1000L/*ns*/

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
        val updateTime = preferenceHelper.getCacheTime()
        if(updateTime != null  && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            fetchFromDatabase()
        } else {
            fetchFromRemote()
        }
    }

    fun forceRefreshCache() {
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        _loading.value = true
        disposable.add(
            dogsService.getDogs()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<DogBreed>>() {
                    override fun onSuccess(t: List<DogBreed>) {
                        storeDogsLocally(t)
                        Toast.makeText(getApplication(), "Dogs retrieved from an endpoint", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        _loading.value = false
                        _dogsLoadError.value = true
                        e.printStackTrace()
                    }

                })
        )

    }

    private fun fetchFromDatabase() {
        _loading.value = true
        launch {
            val dogs = DogDatabase(getApplication()).dogDao().getAllDogs()
            dogsRetrieved(dogs)
            Toast.makeText(getApplication(), "Dogs retrieved from database", Toast.LENGTH_SHORT).show()
        }

    }

    private fun dogsRetrieved(dogsList: List<DogBreed>) {
        _dogs.value = dogsList
        _loading.value = false
        _dogsLoadError.value = false
    }

    private fun storeDogsLocally(list: List<DogBreed>) {
        launch {
            val dao = DogDatabase(getApplication()).dogDao()
            dao.deleteAll()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while(i < list.size) {
                list[i].uuid = result[i].toInt()
                ++i
            }
            dogsRetrieved(list)
        }
        preferenceHelper.saveCacheTime(System.nanoTime())
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}