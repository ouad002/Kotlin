package com.example.automacorp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.model.RoomDto
import com.example.automacorp.model.RoomService
import com.example.automacorp.model.RoomViewModel
import com.example.automacorp.ui.theme.AutomacorpTheme
import android.net.Uri // Correct import
import kotlin.math.round

class RoomActivity : ComponentActivity() {

    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Fetch room details from the intent
        val param = intent.getStringExtra(MainActivity.ROOM_PARAM)
        val room = RoomService.findByNameOrId(param)
        val viewModel = RoomViewModel(room)

        // Function to handle saving the room
        val onRoomSave: () -> Unit = {
            if (viewModel.room != null) {
                val roomDto: RoomDto = viewModel.room as RoomDto
                RoomService.updateRoom(roomDto.id, roomDto)
                Toast.makeText(baseContext, "Room ${roomDto.name} was updated", Toast.LENGTH_LONG).show()
                startActivity(Intent(baseContext, MainActivity::class.java))
            }
        }

        // Function to navigate back to MainActivity
        val navigateBack: () -> Unit = {
            startActivity(Intent(baseContext, MainActivity::class.java))
        }

        // Function to open the RoomListActivity
        val openRoomList: () -> Unit = {
            startActivity(Intent(this, RoomListActivity::class.java))
        }

        // Function to send an email
        val sendEmail: () -> Unit = {
            val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:wadieboussetta2002@gmail.com"))
            startActivity(intent)
        }

        // Function to open the GitHub page
        val openGithub: () -> Unit = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ouad002"))
            startActivity(intent)
        }

        setContent {
            AutomacorpTheme {
                Scaffold(
                    topBar = {
                        AutomacorpTopAppBar(
                            title = "Room",
                            returnAction = navigateBack,
                            openRoomList = openRoomList,
                            sendEmail = sendEmail,
                            openGithub = openGithub
                        )
                    },
                    floatingActionButton = { RoomUpdateButton(onRoomSave) },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    if (viewModel.room != null) {
                        RoomDetail(viewModel, Modifier.padding(innerPadding))
                    } else {
                        NoRoom(Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomUpdateButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = stringResource(R.string.act_room_save),
            )
        },
        text = { Text(text = stringResource(R.string.act_room_save)) }
    )
}

@Composable
fun NoRoom(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.act_room_none),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RoomDetail(model: RoomViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.act_room_name),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = model.room?.name ?: "",
            onValueChange = { model.room?.name = it },
            label = { Text(text = stringResource(R.string.act_room_name)) },
            modifier = Modifier.fillMaxWidth()
        )

        // Display the current temperature (non-editable)
        Text(
            text = stringResource(R.string.act_room_current_temperature) + ": ${model.room?.currentTemperature ?: "N/A"} °C",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Display the target temperature (editable)
        Text(
            text = stringResource(R.string.act_room_target_temperature) + ": ${model.room?.targetTemperature?.toString() ?: "N/A"} °C",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Slider to adjust the target temperature
        Slider(
            value = model.room?.targetTemperature?.toFloat() ?: 18.0f,
            onValueChange = { model.room?.targetTemperature = it.toDouble() },
            valueRange = 10f..28f,
            steps = 0,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer
            )
        )

        // Display the rounded target temperature value below the slider
        Text(
            text = (round((model.room?.targetTemperature ?: 18.0) * 10) / 10).toString() + " °C",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoomDetailPreview() {
    // Create a mock RoomDto object for preview
    val mockRoom = RoomDto(
        id = 1L,
        name = "A1 Meeting Room",
        currentTemperature = 21.5,
        targetTemperature = 22.0,
        windows = emptyList()
    )

    // Create a RoomViewModel for the preview
    val mockViewModel = RoomViewModel(mockRoom)

    AutomacorpTheme {
        RoomDetail(mockViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun NoRoomPreview() {
    AutomacorpTheme {
        NoRoom()
    }
}



