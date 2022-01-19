package com.kflower.gameworld.common

import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Base64
import android.view.View
import java.io.ByteArrayOutputStream

class Utils {
    fun  bitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        val result= Base64.encodeToString(b, Base64.DEFAULT)
        return  result;
    }

}