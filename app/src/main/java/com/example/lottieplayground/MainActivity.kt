package com.example.lottieplayground

import android.app.Activity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), SensorEventListener {

    internal lateinit var groovy_walk: LottieAnimationView
    lateinit var swipe_anim: LottieAnimationView
    internal lateinit var thumb_up: LottieAnimationView
    internal lateinit var thumb_down: LottieAnimationView
    internal lateinit var toggle: LottieAnimationView
    internal var flag = 0
    var running = false
    var sensorManager: SensorManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        //Groovy Walk
        groovy_walk = findViewById(R.id.groovy_walk)
        groovy_walk.setOnClickListener { changeGroovyState() }

        //Swipe anim
        swipe_anim = findViewById(R.id.swipe_anim)
        swipe_anim.repeatCount = LottieDrawable.INFINITE

        //Thumbs Up Button
        thumb_up = findViewById(R.id.lav_thumbUp)
        thumb_up.setOnClickListener {
            thumb_down.progress = 0f
            thumb_down.pauseAnimation()
            thumb_up.playAnimation()
            Toast.makeText(this@MainActivity, "Cheers!!", Toast.LENGTH_SHORT).show()
        }

        //Thumbs Down Button
        thumb_down = findViewById(R.id.lav_thumbDown)
        thumb_down.setOnClickListener {
            thumb_up.progress = 0f
            thumb_up.pauseAnimation()
            thumb_down.playAnimation()
            Toast.makeText(this@MainActivity, "Boo!!", Toast.LENGTH_SHORT).show()
        }

        //Toggle Switch
        toggle = findViewById(R.id.lav_toggle)
        toggle.setOnClickListener { changeToggleState() }
    }

    override fun onResume() {
        super.onResume()
        running = true
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (running) {
            stepsValue.setText("" + event.values[0])
        }
    }

    private fun changeGroovyState() {
        if (flag == 0) {
            groovy_walk.playAnimation()
            groovy_walk.repeatCount = LottieDrawable.INFINITE
            flag = 1
        } else {
            groovy_walk.pauseAnimation()
            flag = 0
        }
    }

    private fun changeToggleState() {
        if (flag == 0) {
            toggle.setMinAndMaxProgress(0f, 0.5f) //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
            toggle.playAnimation()
            flag = 1
        } else {
            toggle.setMinAndMaxProgress(0.5f, 1f)
            toggle.playAnimation()
            flag = 0
        }
    }
}
