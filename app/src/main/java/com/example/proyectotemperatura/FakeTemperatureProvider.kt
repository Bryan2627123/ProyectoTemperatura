package com.example.proyectotemperatura

object FakeTemperatureProvider {
    private var currentTemperature = 25f

    fun getNextTemperature(): Float {
        currentTemperature += 2f
        if (currentTemperature > 45f) {
            currentTemperature = 25f
        }
        return currentTemperature
    }
}
