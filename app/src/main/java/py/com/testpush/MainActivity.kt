package py.com.testpush

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import py.com.testpush.ui.theme.TestPushTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar Firebase
        FirebaseApp.initializeApp(this)
        getToken()
        setContent {
            TestPushTheme {
                RequestNotificationPermission()
            }
        }
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Obtener el token falló", task.exception)
                return@addOnCompleteListener
            }

            // Obtener el nuevo token
            val token = task.result
            Log.d("FCM", "Token de registro: $token")

            // Aquí puedes enviar el token a tu servidor si es necesario
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        var permissionGranted by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                permissionGranted = isGranted
            }
        )

        // Solicitar el permiso
        Button(onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
            Text("Solicitar permiso de notificación")
        }

        if (permissionGranted) {
            Text(text = "Permiso de notificación otorgado")
        } else {
            Text(text = "Permiso de notificación denegado")
        }
    } else {
        Text(text = "El permiso de notificación no es necesario en esta versión de Android")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RequestNotificationPermission()
}