package com.example.automacorp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.automacorp.ui.theme.AutomacorpTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val ROOM_PARAM = "com.automacorp.room.attribute"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onSayHelloButtonClick: (name: String) -> Unit = { name ->
            val intent = Intent(this, RoomActivity::class.java).apply {
                putExtra(ROOM_PARAM, name)
            }
            startActivity(intent)
        }

        setContent {
            AutomacorpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(onClick = onSayHelloButtonClick, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Greeting(onClick: (name: String) -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        AppLogo(Modifier.padding(bottom = 16.dp)) // Adding AppLogo here

        // Declaring the state for the name field
        var name by remember { mutableStateOf("") } // mutableStateOf state for text field

        // Fixing the ambiguity issue by using the String version of OutlinedTextField
        OutlinedTextField(
            value = name, // Passing the name value to the text field
            onValueChange = { newName -> name = newName }, // Updating the state with the new value
            placeholder = { Text(stringResource(R.string.act_room_name)) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(
            onClick = { onClick(name) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.act_main_open))
        }
    }
}

@Composable
fun AppLogo(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.ic_logo),
        contentDescription = stringResource(R.string.app_logo_description),
        modifier = modifier
            .paddingFromBaseline(top = 100.dp)
            .height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AutomacorpTheme {
        Greeting(onClick = {})
    }
}




