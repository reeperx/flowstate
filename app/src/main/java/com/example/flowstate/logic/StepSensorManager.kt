package com.example.flowstate.logic

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface IStepSensorManager {
    val spm: StateFlow<Int>
    fun startListening()
    fun stopListening()
}

class StepSensorManager(context: Context) : IStepSensorManager, SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val stepCounter = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    
    private val _spm = MutableStateFlow(0)
    override val spm = _spm.asStateFlow()

    private var initialSteps = -1f
    private var startTime = 0L

    override fun startListening() {
        stepCounter?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
            startTime = System.currentTimeMillis()
        }
    }

    override fun stopListening() {
        sensorManager?.unregisterListener(this)
        initialSteps = -1f
        _spm.value = 0
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0]
            if (initialSteps == -1f) {
                initialSteps = totalSteps
                startTime = System.currentTimeMillis()
                return
            }

            val currentSteps = totalSteps - initialSteps
            val elapsedMinutes = (System.currentTimeMillis() - startTime) / 60000f
            
            if (elapsedMinutes > 0) {
                _spm.value = (currentSteps / elapsedMinutes).toInt()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

class MockStepSensorManager : IStepSensorManager {
    override val spm = MutableStateFlow(120).asStateFlow()
    override fun startListening() {}
    override fun stopListening() {}
}

val LocalStepSensorManager = staticCompositionLocalOf<IStepSensorManager> { error("No StepSensorManager provided") }
