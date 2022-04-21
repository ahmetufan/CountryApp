package com.example.kotlincountryap.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlincountryap.model.Country
import com.example.kotlincountryap.service.CountryApiService
import com.example.kotlincountryap.service.CountryDatabase
import com.example.kotlincountryap.util.CustomSharedPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : BaseViewModel(application) {
    private val countryApiService = CountryApiService()
    private val compositeDisposable = CompositeDisposable()

    private var customPreferences = CustomSharedPreferences(getApplication())
    private var refreshTime = 10 * 60 * 1000 * 1000 * 1000L  //10 dakika nanoSaniyesi

    val countries = MutableLiveData<List<Country>>()
    val countryError = MutableLiveData<Boolean>()
    val countryLoadding = MutableLiveData<Boolean>()

    fun refreshData() {

        val updateTime = customPreferences.getTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            //10 dk'yı geçmediyse
            getDataFromSQLite()
        } else {
            // 10 dk'yı geçtiyse
            getDataFromAPI()
        }

    }

    fun refreshFromAPI() {
 //her refresh yapıldığında api'den yükleme yapacak.swipeRefreshLayout.setOnRefreshListener'de  içinde ayarlama yapıldı
        getDataFromAPI()

    }

    private fun getDataFromSQLite() {
        countryLoadding.value=true
        launch {
            val countries=CountryDatabase(getApplication()).countryDao().getAllCountries()
            showCountries(countries)
            Toast.makeText(getApplication(),"Countries From SQLite",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getDataFromAPI() {

        countryLoadding.value = true

        compositeDisposable.add(
            countryApiService.getData()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(t: List<Country>) {
                        storeInSQLite(t)
                        Toast.makeText(getApplication(),"Countries From API",Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        countryLoadding.value = false
                        countryError.value = true
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun showCountries(countryList: List<Country>) {
        countries.value = countryList
        countryError.value = false
        countryLoadding.value = false
    }

    private fun storeInSQLite(list: List<Country>) {

        launch {
            val dao = CountryDatabase(getApplication()).countryDao()
            dao.deleteAllCountries()
            val listLong = dao.insertAll(*list.toTypedArray())  //  Bu satırda country listesindeki herşeyi tek tek ekledik

            var i = 0
            while (i < list.size) {
                list[i].uuid = listLong[i].toInt()
                i++
            }
            showCountries(list)
        }

        customPreferences.saveTime(System.nanoTime())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

}