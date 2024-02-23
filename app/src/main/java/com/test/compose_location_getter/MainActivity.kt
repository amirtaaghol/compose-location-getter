package com.test.compose_location_getter

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.core.content.ContextCompat
import com.test.compose_location_getter.ui.theme.ComposelocationgetterTheme

class MainActivity : ComponentActivity() {

    private lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val multiplePermissionsRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val postNotificationsGranted = permissions[android.Manifest.permission.POST_NOTIFICATIONS] ?: false

        if (!fineLocationGranted) {
            Toast.makeText(this, "Location permission is denied", Toast.LENGTH_SHORT).show()
        } else if (!postNotificationsGranted) {
            Toast.makeText(this, "Notification permission is denied", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Starting the location service...", Toast.LENGTH_SHORT).show()
            startLocationService()
        }
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposelocationgetterTheme {

                val allLocations by mainViewModel.allLocations.observeAsState(emptyList())
                LocationList(locations = allLocations)

            }
        }

        val locationDao = LocationDatabase.getInstance(applicationContext).locationDao()
        val mainRepository = MainRepository(locationDao)
        mainViewModel = MainViewModel(mainRepository)

        mainViewModel.allLocations.observe(this) { locations ->
            Log.d("LOCATIONS" , locations.toString())

        }
        checkPermissions()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkPermissions() {
        val permissions = arrayOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS
        )

        val permissionsGranted = mutableListOf<String>()

        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(this, permission)
                == PackageManager.PERMISSION_GRANTED
            ) {
                permissionsGranted.add(permission)
            } else {
                if (shouldShowRequestPermissionRationale(permission)) {
                    showPermissionRationale(permission)
                } else {
                    multiplePermissionsRequest.launch(permissions)
                }
            }
        }

        if (permissionsGranted.size == 2) {
            Toast.makeText(this , "Starting the location service..." , Toast.LENGTH_SHORT).show()
            startLocationService()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showPermissionRationale(permission: String) {
        var permissionName : String
        if (permission == android.Manifest.permission.ACCESS_FINE_LOCATION) {
            permissionName = "Location Permission"
        }else{
            permissionName = "Notification Permission"
        }
        AlertDialog.Builder(this)
            .setTitle("$permissionName is Needed")
            .setMessage("This app requires $permissionName to access your location to fetch current location data.")
            .setPositiveButton("OK") { _, _ ->
                multiplePermissionsRequest.launch(arrayOf(permission))
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "$permissionName denied", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun startLocationService() {
        val serviceIntent = Intent(this, LocationService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}