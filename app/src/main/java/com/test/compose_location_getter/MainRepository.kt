package com.test.compose_location_getter

import androidx.lifecycle.LiveData

class MainRepository(private val locationDao : LocationDao) {
    val allLocations : LiveData<List<Location>> = locationDao.getAllLocations()

    suspend fun insertLocation(location: Location) {
        locationDao.insert(location)
    }
}