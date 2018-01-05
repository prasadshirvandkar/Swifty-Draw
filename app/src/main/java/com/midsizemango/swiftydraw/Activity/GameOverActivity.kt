package com.midsizemango.swiftydraw.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.midsizemango.swiftydraw.Helper.ScoreHelper
import com.midsizemango.swiftydraw.R
import com.midsizemango.swiftydraw.Animation.StarAnimationView

/**
 * Created by ABC on 12/27/2017.
 */
class GameOverActivity: AppCompatActivity() {

    var scoreText: TextView? = null
    var leaderboard: FrameLayout? = null
    var playAgain: FrameLayout? = null
    var mainMenu: FrameLayout? = null
    var prefsth: SharedPreferences? = null
    var back: ImageButton? = null
    var starAnim: StarAnimationView? = null
    var textCompleted: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntroActivity().fullScreenWindow(window)
        setContentView(R.layout.activity_game_over)

        playAgain = findViewById(R.id.play_again)
        mainMenu = findViewById(R.id.main_menu)
        back = findViewById(R.id.back_game_over)
        starAnim = findViewById(R.id.animated_view)
        textCompleted = findViewById(R.id.text_completed)

        val cguess = ScoreHelper.correctGuesses
        val tplays = ScoreHelper.timesPlayed - 1
        textCompleted!!.text = cguess.toString()+" / 8"


        back!!.setOnClickListener {
            onBackPressed()
        }

        playAgain!!.setOnClickListener {
            clearValues()
            val intent = Intent(applicationContext, StartGameActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finishAffinity()
        }

        mainMenu!!.setOnClickListener {
            returnBack()
        }
    }

    private fun clearValues(){
        ScoreHelper.timesPlayed = 1
        ScoreHelper.correctGuesses = 0
        ScoreHelper.labels.clear()
    }

    private fun returnBack(){
        clearValues()
        val intent = Intent(applicationContext, IntroActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    override fun onResume() {
        super.onResume()
        IntroActivity().fullScreenWindow(window)
        starAnim!!.resume()
    }

    override fun onPause() {
        super.onPause()
        starAnim!!.pause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        returnBack()
    }

}