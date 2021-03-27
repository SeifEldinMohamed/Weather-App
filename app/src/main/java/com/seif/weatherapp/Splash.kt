package com.seif.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_splash.*

class Splash : AppCompatActivity() {
    var splashtime = 2100L
    var durationAnimation = 1900L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // make window with no title bar
        window.requestFeature(Window.FEATURE_NO_TITLE)
        // make window full screen for better view.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)
        // animate the company name ( fade in and out )
        txt_Inc.alpha = 0f
        txt_Inc.animate().setDuration(durationAnimation).alpha(1.5f)
        txt_weather.alpha = 0f
        txt_weather.animate().setDuration(durationAnimation).alpha(1.5f)
        Handler().postDelayed({
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
            finish()
        }, splashtime)

    }
}