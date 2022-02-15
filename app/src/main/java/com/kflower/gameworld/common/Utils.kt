package com.kflower.gameworld.common

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Base64
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory




fun Context.fragmentActivity(): FragmentActivity? {
    var curContext = this
    var maxDepth = 20
    while (--maxDepth > 0 && curContext !is FragmentActivity) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if(curContext is FragmentActivity)
        curContext
    else
        null
}

fun Context.lifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext as LifecycleOwner
    } else {
        null
    }
}

fun Bitmap.bitMapToString(): String {
    val byteArray = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArray)
    val b: ByteArray = byteArray.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT);
}

fun String.toBitMap(): Bitmap {
    val decodedString: ByteArray = Base64.decode(this, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
}