package com.example.automacorp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.RoomViewModel
import com.example.automacorp.service.ApiServices
import com.example.automacorp.ui.theme.AutomacorpTheme
import com.example.automacorp.ui.theme.PurpleGrey80

class RoomListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewModel initialization
        val viewModel: RoomViewModel by viewModels()

        setContent {
            // Collecting the state from the ViewModel using `collectAsState()`
            val roomsState by viewModel.roomsState.collectAsState()

            // Launching the request to load rooms in the background
            LaunchedEffect(Unit) {
                viewModel.findAll() // Fetch rooms using the ViewModel
            }

            // Checking if there's an error and showing the appropriate UI
            if (roomsState.error != null) {
                // Show an empty list in case of an error
                RoomList(
                    rooms = emptyList(),
                    navigateBack = { finish() },
                    openRoom = { openRoom(it) }
                )
                Toast.makeText(
                    applicationContext,
                    "Error on rooms loading: ${roomsState.error}",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Show the list of rooms when successfully loaded
                RoomList(
                    rooms = roomsState.rooms,
                    navigateBack = { finish() },
                    openRoom = { openRoom(it) }
                )
            }
        }
    }

    // Method to open RoomDetailActivity with room ID
    private fun openRoom(roomId: Long) {
        val intent = Intent(this, RoomDetailActivity::class.java).apply {
            putExtra(RoomDetailActivity.ROOM_ID, roomId)
        }
        startActivity(intent)
    }
}

@Composable
fun RoomList(
    rooms: List<RoomDto>,
    navigateBack: () -> Unit,
    openRoom: (id: Long) -> Unit
) {
    AutomacorpTheme {
        Scaffold(
            topBar = { AutomacorpTopAppBar(title = "Rooms", returnAction = navigateBack) }
        ) { innerPadding ->
            if (rooms.isEmpty()) {
                Text(
                    text = "No rooms found",
                    modifier = Modifier.padding(innerPadding)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(innerPadding),
                ) {
                    items(rooms, key = { it.id }) {
                        RoomItem(
                            room = it,
                            modifier = Modifier.clickable { openRoom(it.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RoomItem(room: RoomDto, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, PurpleGrey80)
    ) {
        Row(
            modifier = modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Target temperature: ${room.targetTemperature?.toString() ?: "?"}°",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${room.currentTemperature?.toString() ?: "?"}°",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomacorpTopAppBar(
    title: String,
    returnAction: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = returnAction) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {},
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.White)
    )
}




