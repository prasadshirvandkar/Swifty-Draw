package com.midsizemango.swiftydraw.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window

import android.widget.FrameLayout
import com.midsizemango.swiftydraw.R

/**
 * Created by ABC on 12/27/2017.
 */

class IntroActivity: AppCompatActivity(){

    var play: FrameLayout? = null
    var exit: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenWindow(window)
        setContentView(R.layout.activity_intro)

        play = findViewById(R.id.play)
        exit = findViewById(R.id.exit)

        play!!.setOnClickListener {
            val intent = Intent(applicationContext, StartGameActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_stay)
            finish()
        }

        exit!!.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        fullScreenWindow(window)

    }

    fun fullScreenWindow(window: Window){
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        window.decorView.setOnSystemUiVisibilityChangeListener {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}