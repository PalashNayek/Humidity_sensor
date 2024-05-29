package com.palash.humidity_sensor.repository

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class SensorRepository @Inject constructor(private val sensorManager: SensorManager) {

    private val _humidityData = MutableLiveData<Float?>()
    val humidityData: LiveData<Float?> = _humidityData

    private val humidityListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            event?.let {
                if (it.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
                    _humidityData.postValue(it.values[0])
                }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            // No action needed
        }
    }

    fun startListening() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)
        if (sensor != null) {
            sensorManager.registerListener(humidityListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        } else {
            _humidityData.postValue(null)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(humidityListener)
    }
}