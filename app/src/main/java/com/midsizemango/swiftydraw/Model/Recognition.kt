package com.midsizemango.swiftydraw.Model

import android.annotation.SuppressLint
import android.graphics.RectF

/**
 * Created by ABC on 12/28/2017.
 */

class Recognition(val id: String?, val title: String?, val confidence: Float?, private var location: RectF?) {

    fun getLocation(): RectF {
        return RectF(location)
    }

    fun setLocation(location: RectF) {
        this.location = location
    }

    @SuppressLint("DefaultLocale")
    override fun toString(): String {
        var resultString = ""
        if (id != null) {
            //resultString += "[" + id + "] ";
        }

        if (title != null) {
            resultString += title
        }

        if (confidence != null) {
            //resultString += "(Confidence :" + String.format(" %.1f%% ", confidence * 100.0f) + ")";
        }

        if (location != null) {
            //resultString += location + " ";
        }

        return resultString.trim { it <= ' ' }
    }
}