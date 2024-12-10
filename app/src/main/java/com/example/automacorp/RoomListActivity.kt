package com.example.automacorp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat.startActivity
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.RoomService
import com.example.automacorp.ui.theme.AutomacorpTheme
import com.example.automacorp.ui.theme.PurpleGrey80

class RoomListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "Rooms",
                            returnAction = { finish() } // This will return to the previous screen when the back button is pressed
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    RoomListScreen(innerPadding)
                }
            }
        }
    }
}

@Composable
fun RoomListScreen(innerPadding: PaddingValues) {
    val context = LocalContext.current // Get the context

    LazyColumn(
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(innerPadding),
    ) {
        val rooms = RoomService.findAll() // Fetch all rooms from the service
        items(rooms, key = { it.id }) { room ->
            RoomItem(
                room = room,
                modifier = Modifier.clickable { openRoom(context, room.id) } // Pass context to openRoom
            )
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
                    text = "Target temperature : " + (room.targetTemperature?.toString() ?: "?") + "°",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = (room.currentTemperature?.toString() ?: "?") + "°",
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

fun openRoom(context: Context, roomId: Long) {
    val intent = Intent(context, RoomDetailActivity::class.java).apply {
        putExtra(RoomDetailActivity.ROOM_ID, roomId) // Pass the room ID
    }
    startActivity(context, intent, null) // Correct usage of startActivity with context
}

@Preview(showBackground = true)
@Composable
fun RoomListScreenPreview() {
    AutomacorpTheme {
        RoomListScreen(innerPadding = PaddingValues(16.dp)) // Mock preview of the list
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutomacorpTopAppBar(
    title: String,
    returnAction: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = { returnAction() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
        actions = {},
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White
        )
    )
}


