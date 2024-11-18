package com.example.proyectotemperatura

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun MonitorScreen(temperature: Double) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Convierto temperature a Float al pasarlo a TemperatureCircle
        TemperatureCircle(
            temperature = temperature.toFloat(),
            criticalLimit = 40.0f, // Límite crítico definido como Float
            riskyLimit = 30.0f    // Límite riesgoso definido como Float
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Temperatura actual: ${"%.2f".format(temperature)} °C",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}
