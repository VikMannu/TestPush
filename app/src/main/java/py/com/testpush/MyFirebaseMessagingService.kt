package py.com.testpush

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Maneja el mensaje recibido
        Log.d("FCM", "Mensaje recibido de: ${remoteMessage.from}")

        // Verifica si el mensaje contiene datos
        remoteMessage.data.isNotEmpty().let {
            Log.d("FCM", "Datos del mensaje: ${remoteMessage.data}")
        }

        // Verifica si el mensaje contiene una notificación
        remoteMessage.notification?.let {
            Log.d("FCM", "Título de la notificación: ${it.title}")
            Log.d("FCM", "Cuerpo de la notificación: ${it.body}")
            showNotification(it.title, it.body)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "Nuevo token de registro: $token")
        // Enviar el token al servidor si es necesario
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "default_notification_channel" // Reemplaza con tu ID de canal
        val notificationId = 0 // ID de la notificación

        // Crea el canal de notificación si es necesario (Android 8.0 y superior)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Your Channel Name"
            val channelDescription = "Your Channel Description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Construye la notificación
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background) // Reemplaza con tu icono
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Muestra la notificación
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }
}
