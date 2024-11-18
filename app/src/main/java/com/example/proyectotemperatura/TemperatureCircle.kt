package com.example.proyectotemperatura

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape

@Composable
fun TemperatureCircle(temperature: Float, criticalLimit: Float, riskyLimit: Float) {
    val color = when {
        temperature >= criticalLimit -> Color.Red
        temperature >= riskyLimit -> Color.Yellow
        else -> Color.Green
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .background(color, shape = CircleShape)
    ) {
        Text(
            text = "${temperature}°C",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White
        )
    }
}
