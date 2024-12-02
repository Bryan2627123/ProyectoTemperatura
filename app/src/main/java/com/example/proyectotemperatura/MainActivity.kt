package com.example.proyectotemperatura

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import com.example.proyectotemperatura.ui.theme.ProyectoTemperaturaTheme
import com.google.firebase.database.*

class MainActivity : ComponentActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Solicitar permiso para notificaciones
        requestNotificationPermission()

        // Inicializa la referencia a la base de datos
        database = FirebaseDatabase.getInstance().reference

        // Crear canal de notificaciones
        createNotificationChannel()

        setContent {
            ProyectoTemperaturaTheme {
                var temperature by remember { mutableStateOf(0.0) } // Valor inicial de la temperatura

                // Escucha cambios en tiempo real en Firebase
                LaunchedEffect(Unit) {
                    database.child("temperatura").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val value = snapshot.getValue(Double::class.java)
                            if (value != null) {
                                temperature = value
                                if (temperature >= 40.0) {
                                    showNotification(temperature)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Manejo de errores de Firebase
                        }
                    })
                }

                Surface {
                    MonitorScreen(temperature = temperature)
                }
            }
        }
    }

    // Crear un canal de notificaciones con sonido
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Temperature Alerts"
            val descriptionText = "Notificaciones para alertas de temperatura"
            val importance = NotificationManager.IMPORTANCE_HIGH

            // En esta parte se configura el sonido para la notificación
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val channel = NotificationChannel("TEMP_ALERT_CHANNEL", name, importance).apply {
                description = descriptionText
                setSound(soundUri, null) // Asigna el sonido al canal
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Aquí se muestra la notificación
    @SuppressLint("MissingPermission")
    private fun showNotification(temperature: Double) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, "TEMP_ALERT_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Usa el ícono de tu aplicación
            .setContentTitle("¡Alerta de Temperatura!")
            .setContentText("La temperatura ha alcanzado ${"%.2f".format(temperature)}°C.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(soundUri) // Asigna sonido a la notificación

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }

    // Acá se solicita el permiso para las notificaciones
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }
    }
}
