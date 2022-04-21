package com.example.kotlincountryap.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlincountryap.model.Country

@Database(entities = [Country::class], version = 1)
abstract class CountryDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    //Singleton
    companion object {

        @Volatile
        private var instance: CountryDatabase? = null


        private fun makeDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,
            CountryDatabase::class.java, "countrydatabase").build()

// instance adında bir obje oluşturuluyor. bunu companion object içerisinde yapıyoruz ki diğer scope'lerden ulaşabilelim
//   instance varmı yok mu kontrol ediyoruz invoke fonksiyonu içerisinde, eğer varsa instance dönüyor ,
        //  eğer yoksa makeDatabase fonksiyonunu çağırıyoruz ve instance'mizi ona senkronize şekilde eşitliyor

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }

        private val lock = Any()
    }
}