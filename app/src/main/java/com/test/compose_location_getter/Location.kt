package com.test.compose_location_getter

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations_table")
data class Location(
    @PrimaryKey(autoGenerate = true)
    val id : Long =0,
    val time : String,
    val altitude: Double,
    val longitude: Double

)
