package com.example.automacorp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.automacorp.model.RoomService
import com.example.automacorp.model.RoomDto
import com.example.automacorp.ui.theme.AutomacorpTheme

class RoomDetailActivity : ComponentActivity() {

    companion object {
        const val ROOM_ID = "com.automacorp.room_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val roomId = intent.getLongExtra(ROOM_ID, -1L)
        val room = RoomService.findById(roomId) // Fetch room details by ID

        setContent {
            AutomacorpTheme {
                if (room != null) {
                    RoomDetail(room = room) // Show room details
                } else {
                    Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity if room not found
                }
            }
        }
    }
}

@Composable
fun RoomDetail(room: RoomDto) {
    // Get the context from LocalContext to call non-composable operations
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "Room Name: ${room.name}", style = MaterialTheme.typography.titleMedium)
        Text(text = "Target Temperature: ${room.targetTemperature}°C", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Current Temperature: ${room.currentTemperature}°C", style = MaterialTheme.typography.bodyLarge)

        // Back button to return to RoomListActivity
        Button(onClick = {
            // Using context to call onBackPressed() for navigation
            (context as? ComponentActivity)?.onBackPressed()
        }) {
            Text(text = "Back to Room List")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetailActivityPreview() {
    val mockRoom = RoomDto(
        id = 1L,
        name = "A1 Meeting Room",
        currentTemperature = 22.0,
        targetTemperature = 22.5,
        windows = emptyList()
    )

    AutomacorpTheme {
        RoomDetail(mockRoom)
    }
}



