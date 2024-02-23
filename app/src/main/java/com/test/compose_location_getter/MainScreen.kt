package com.test.compose_location_getter

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationList(locations: List<Location>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Location App") },
            )
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    content = {
                        item {
                            Spacer(modifier = Modifier.height(56.dp))
                        }
                        items(locations, key = { location -> location.id }) { location ->
                            LocationItem(location = location)
                            Divider(Modifier.height(2.dp))
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun LocationItem(location: Location) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Timestamp:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = location.time,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Altitude:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = location.altitude.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Longitude:",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = location.longitude.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}