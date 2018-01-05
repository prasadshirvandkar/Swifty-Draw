package com.midsizemango.swiftydraw.Activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.midsizemango.swiftydraw.Classification.ImageClassifier
import com.midsizemango.swiftydraw.Helper.ImageHelper
import com.midsizemango.swiftydraw.Helper.ScoreHelper
import com.midsizemango.swiftydraw.Model.Recognition
import com.midsizemango.swiftydraw.R
import com.rm.freedrawview.FreeDrawView
import com.rm.freedrawview.PathDrawnListener
import com.rm.freedrawview.PathRedoUndoCountChangeListener
import com.rm.freedrawview.ResizeBehaviour
import java.util.Random
import java.util.concurrent.TimeUnit

/**
 * Created by ABC on 12/27/2017.
 */

class RunningGameActivity: AppCompatActivity() {

    private var recognitionList: List<Recognition>? = null
    private var imageClassifier: ImageClassifier? = null
    private var displayText: TextView? = null
    internal var freedrawView: FreeDrawView? = null
    var clear: FrameLayout? = null
    var undo: FrameLayout? = null
    var redo: FrameLayout? = null
    var time: TextView? = null
    var labelTextGame: TextView? = null
    var next: Button? = null
    var labelExtra: String? = null
    var labelTextString: String? = null
    var timer: CountDownTimer? = null
    var milltoFinish: Long? = null
    var timeArray = intArrayOf(25)
    var timeinms:Long = 25000
    var pauseButton: ImageButton? = null
    var cgStatus = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        IntroActivity().fullScreenWindow(window)
        setContentView(R.layout.activity_running_game)

        labelExtra = intent.extras.getString("labelText")
        labelTextString = if(labelExtra != null) {
            labelExtra!!
        }else{
            "None"
        }

        freedrawView = findViewById(R.id.draw_view)
        displayText = findViewById(R.id.sayitout)
        clear = findViewById(R.id.clear)
        undo = findViewById(R.id.undo)
        redo = findViewById(R.id.redo)
        time = findViewById(R.id.time)
        labelTextGame = findViewById(R.id.label_text_game)
        next = findViewById(R.id.next_text)
        pauseButton = findViewById(R.id.pause)
        next!!.isEnabled = false

        imageClassifier = ImageClassifier(applicationContext)

        labelTextGame!!.text = "Draw "+labelTextString

        startTimer(timeinms)

        freedrawView!!.paintColor = Color.BLACK
        freedrawView!!.setPaintWidthPx(resources.getDimensionPixelSize(R.dimen.paint_width).toFloat())
        freedrawView!!.setPaintWidthDp(resources.getDimension(R.dimen.paint_width))
        freedrawView!!.paintAlpha = 255
        freedrawView!!.resizeBehaviour = ResizeBehaviour.CROP
        freedrawView!!.setPathRedoUndoCountChangeListener(object : PathRedoUndoCountChangeListener {
            override fun onUndoCountChanged(undoCount: Int) {}

            override fun onRedoCountChanged(redoCount: Int) {}
        })

        freedrawView!!.setOnPathDrawnListener(object : PathDrawnListener {
            override fun onNewPathDrawn() {
                freedrawView!!.getDrawScreenshot(object : FreeDrawView.DrawCreatorListener {
                    override fun onDrawCreated(draw: Bitmap) {
                        val resizedBitmap = Bitmap.createScaledBitmap(draw, ImageClassifier.INPUT_SIZE, ImageClassifier.INPUT_SIZE, false)
                        recognitionList = imageClassifier!!.recognizeImage(ImageHelper.bitmapToFloat(resizedBitmap))
                        val sen = sayItOut()
                        displayText!!.text = sen
                        if(!recognitionList!!.isEmpty()){
                            if(labelTextString!!.toLowerCase() == recognitionList!![0].title) {
                                if(cgStatus == 0) {
                                    ScoreHelper.correctGuesses++
                                    cgStatus = 1
                                }
                                Log.d("TAG_SD", "Correct Guesses: "+ScoreHelper.correctGuesses)
                                next!!.isEnabled = true
                                next!!.visibility = View.VISIBLE
                                displayText!!.text = "Correct"

                                Toast.makeText(applicationContext, "Correct", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    override fun onDrawCreationError() {
                        Toast.makeText(applicationContext, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onPathStart() {
            }
        })

        clear!!.setOnClickListener {
            val drawView = freedrawView
            drawView!!.clearDrawAndHistory()
        }

        undo!!.setOnClickListener {
            val drawView = freedrawView
            drawView!!.undoLast()
        }

        redo!!.setOnClickListener{
            val drawView = freedrawView
            drawView!!.redoLast()
        }

        next!!.setOnClickListener {
            proceedGame()
        }

        pauseButton!!.setOnClickListener {
            onBackPressed()
        }
    }

    private fun sayItOut(): String {
        val patterns = arrayOf("Seem like ", "I see ", "Oh I know this. This is ", "May be ", "Kind of looks like ", "Is this ", "Is this really how you draw ", "This looks awful, but it may be ")
        val generator = Random()
        val rnd_index = generator.nextInt(patterns.size)
        val sentence_select = patterns[rnd_index]
        val sentence_line: String
        sentence_line = if(recognitionList!!.isEmpty()){
            "No Match Found"
        }else{
            sentence_select+recognitionList!![0]+" \nConfidence: "+recognitionList!![0].confidence
        }
        return sentence_line
    }

    fun checkDigit(number: Int): String {
        return if (number <= 9) "0" + number else number.toString()
    }

    fun proceedGame(){
        if (ScoreHelper.timesPlayed > 8) {
            val intent = Intent(applicationContext, GameOverActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_stay)
        } else {
            overridePendingTransition(R.anim.activity_close_stay, R.anim.activity_close_translate)
        }
        ScoreHelper.labels.remove(labelExtra)
        timer!!.cancel()
        finish()
    }

    private fun startTimer(millUntilFinish: Long?){
        timer = object : CountDownTimer(millUntilFinish!!, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                milltoFinish = millisUntilFinished
                val hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milltoFinish!!),
                        TimeUnit.MILLISECONDS.toMinutes(milltoFinish!!) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milltoFinish!!)),
                        TimeUnit.MILLISECONDS.toSeconds(milltoFinish!!) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milltoFinish!!)))

                time!!.text = hms.substring(3,hms.length)
                timeArray[0]--
            }

            override fun onFinish() {
                val time_up = "00:00"
                time!!.text = time_up
                proceedGame()
            }
        }
        timer!!.start()
    }

    override fun onResume() {
        super.onResume()
        IntroActivity().fullScreenWindow(window)
    }

    override fun onBackPressed() {
        timer!!.cancel()
        if (ScoreHelper.timesPlayed != 1){
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.custom_dialog)
            dialog.setCanceledOnTouchOutside(false)
            val textTitle = dialog.findViewById(R.id.dialogTitle) as TextView
            val textMessage = dialog.findViewById(R.id.dialogText) as TextView
            val neutralButton = dialog.findViewById(R.id.neutralButton) as Button
            val negativeButton = dialog.findViewById(R.id.negativeButton) as Button
            val positiveButton = dialog.findViewById(R.id.positiveButton) as Button
            textTitle.text = "Pause"
            textMessage.text = "Do you want to continue with next task or stop the game?"
            positiveButton.text = "Resume"
            negativeButton.text = "Next Task"

            neutralButton.setOnClickListener {
                val intent = Intent(applicationContext, GameOverActivity::class.java)
                intent.putExtra("playmode", "normal")
                startActivity(intent)
                overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_stay)
                finish()
            }

            positiveButton.setOnClickListener {
                dialog.dismiss()
                startTimer(milltoFinish)
            }

            negativeButton.setOnClickListener {
                if (ScoreHelper.timesPlayed > 8) {
                    val intent = Intent(applicationContext, GameOverActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.activity_open_translate, R.anim.activity_close_stay)
                } else {
                    overridePendingTransition(R.anim.activity_close_stay, R.anim.activity_close_translate)
                }
                ScoreHelper.labels.remove(labelExtra)
                finish()
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
