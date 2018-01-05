package com.midsizemango.swiftydraw.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.midsizemango.swiftydraw.Utils.LabelGenerator
import com.midsizemango.swiftydraw.Helper.ScoreHelper
import com.midsizemango.swiftydraw.R
import java.util.Random

/**
 * Created by ABC on 12/27/2017.
 */
class StartGameActivity: AppCompatActivity() {

    var startGame: FrameLayout? = null
    var labelText: TextView? = null
    var timerText: TextView? = null
    var ghelper: LabelGenerator? = null
    var newLabelString: String? = null
    var timer: CountDownTimer? = null
    var prefsth: SharedPreferences? = null
    val r = Random()
    var back: ImageButton? = null
    val timeArray = intArrayOf(3)
    var milltofinish: Long? = null
    var timeText: TextView? = null
    var generatedLabel: String? = null

    var getPlayingOption: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntroActivity().fullScreenWindow(window)
        setContentView(R.layout.activity_game_start)

        ghelper = LabelGenerator(applicationContext)

        startGame = findViewById(R.id.start)
        labelText = findViewById(R.id.label_text)
        timerText = findViewById(R.id.timer_text)
        back = findViewById(R.id.back_game)
        timeText = findViewById(R.id.time_text)
        val playInfo: TextView = findViewById(R.id.play_info)
        playInfo.text = "Iterative Tasks according to Level.\nNot necessary to complete all tasks."

        generatedLabel = getNewLabel()
        newLabelString = generatedLabel!!.toUpperCase()
        labelText!!.text = newLabelString

        if (ScoreHelper.timesPlayed > 1) {
            timerText!!.visibility = View.VISIBLE
            startGame!!.visibility = View.GONE
            playInfo.visibility = View.GONE

            timer = object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    milltofinish = millisUntilFinished
                    val set = "00:" + checkDigit(timeArray[0])
                    timerText!!.text = "Starting in " + set[4] + " seconds"
                    timeArray[0]--
                }

                override fun onFinish() {
                    goIntoGame()
                }
            }
            timer!!.start()
        }

        startGame!!.setOnClickListener {
            goIntoGame()
        }

        back!!.setOnClickListener {
            onBackPressed()
        }
    }

    private fun goIntoGame() {
        ScoreHelper.timesPlayed++
        Log.d("TAG_SD", "TIMES_PLAYED: "+ScoreHelper.timesPlayed)
        val intent = Intent(applicationContext, RunningGameActivity::class.java)
        intent.putExtra("labelText", generatedLabel)
        startActivity(intent)
    }

    override fun onRestart() {
        super.onRestart()
        recreate()
    }

    override fun onResume() {
        super.onResume()
        IntroActivity().fullScreenWindow(window)
    }

    private fun getNewLabel(): String {
        return ghelper!!.getRandomLabel()
    }

    fun checkDigit(number: Int): String {
        return if (number <= 9) "0" + number else number.toString()
    }

    fun startTimer(milltoFinishST: Long?) {
        if (ScoreHelper.timesPlayed > 1) {
            timer = object : CountDownTimer(milltoFinishST!!, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val set = "00:" + checkDigit(timeArray[0])
                    timerText!!.text = "Starting in " + set[4] + " seconds"
                    timeArray[0]--
                }

                override fun onFinish() {
                    goIntoGame()
                }
            }
            timer!!.start()
        }
    }

    override fun onBackPressed() {
        if (ScoreHelper.timesPlayed != 1){
            timer!!.cancel()
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_dialog)
            dialog.setCanceledOnTouchOutside(false)
            val textTitle = dialog.findViewById(R.id.dialogTitle) as TextView
            val textMessage = dialog.findViewById(R.id.dialogText) as TextView
            val neutralButton = dialog.findViewById(R.id.neutralButton) as Button
            val negativeButton = dialog.findViewById(R.id.negativeButton) as Button
            val positiveButton = dialog.findViewById(R.id.positiveButton) as Button
            textTitle.text = "Options"
            textMessage.text = "Do you want to continue playing or stop the game?"

           /* dialog.setOnDismissListener {
                startTimer(milltofinish)
                IntroActivity().fullScreenWindow(window)
            }*/

            neutralButton.setOnClickListener {
                val intent = Intent(applicationContext, GameOverActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_stay)
                ScoreHelper.labels.remove(generatedLabel)
                finish()
            }

            negativeButton.setOnClickListener {
                dialog.dismiss()
                startTimer(milltofinish)
            }

            positiveButton.setOnClickListener {
                dialog.dismiss()
                startTimer(milltofinish)
            }
            dialog.show()
        } else {
            val intent = Intent(applicationContext, IntroActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

}