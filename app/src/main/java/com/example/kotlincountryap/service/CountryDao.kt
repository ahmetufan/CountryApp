package com.example.kotlincountryap.service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlincountryap.model.Country

@Dao
interface CountryDao {

    @Query("SELECT * FROM country")
    suspend fun getAllCountries(): List<Country>

    @Query("SELECT * FROM country  WHERE  uuid= :countryId")
    suspend fun getCountry(countryId: Int): Country

    @Query("DELETE FROM country")
    suspend fun deleteAllCountries()

    @Insert
    suspend fun insertAll(vararg country: Country): List<Long>

}