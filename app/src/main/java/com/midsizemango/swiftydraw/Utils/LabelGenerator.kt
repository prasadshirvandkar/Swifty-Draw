package com.midsizemango.swiftydraw.Utils

import android.content.Context
import com.midsizemango.swiftydraw.Helper.ScoreHelper
import java.io.IOException
import java.util.*


/**
 * Created by ABC on 12/27/2017.
 */
class LabelGenerator(var context: Context){

    private val LABEL_FILE = "retrained_labels_swiftydraw.txt"
    private val CLASS_SIZE = 19
    private var labels: Vector<String>? = null

    init {
        getLabels()
        if (ScoreHelper.timesPlayed > 8) {
            ScoreHelper.timesPlayed = 1
            ScoreHelper.correctGuesses = 0
        }
    }

    fun getRandomLabel(): String{
        val randomGenerator = Random()
        val label: String = ScoreHelper.labels[randomGenerator.nextInt(ScoreHelper.labels.size)]
        return label
    }

    private fun getLabels() {
        labels = Vector(CLASS_SIZE)
        try {
            var labelValue: String?
            context.assets.open(LABEL_FILE).bufferedReader().forEachLine {
                labelValue = it
                labels!!.add(labelValue)
            }
            if(ScoreHelper.timesPlayed == 1) {
                for(i in labels!!){
                    ScoreHelper.labels.add(i)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException("Problem reading label file!", e)
        }
    }
}