package com.kflower.gameworld.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.util.Base64
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.kflower.gameworld.model.AudioBook
import java.lang.Exception
import java.util.*
import kotlin.math.floor


fun Context.fragmentActivity(): FragmentActivity? {
    var curContext = this
    var maxDepth = 20
    while (--maxDepth > 0 && curContext !is FragmentActivity) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is FragmentActivity)
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

fun renderRandomId(): String {
    var id = ""
    val min = 1000
    val max = 9999
    val randomInt =
        floor(Math.random() * (max - min + 1) + min).toInt()
    var uuid: String? = UUID.randomUUID().toString()
    id += (randomInt.toString() + uuid)
    return id
}

fun getDownloadPath(activity: Activity):String{
    var idResult= activity.getPreferences(AppCompatActivity.MODE_PRIVATE).getString(Key.KEY_APP_ID, null);
    val downloadsPath =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path+"/TopTopFiles${idResult}"
    return  downloadsPath
}
fun getDownloadSpecificPath(activity: Activity, audioBookId: String, audioEp: Int): String {
    val file = "${audioBookId}_ID_${audioEp}.mp3"
    return "${getDownloadPath(activity)}/$file"
}
fun String.getAudioIdFromUri(activity: Activity): String {
    var idResult= activity.getPreferences(AppCompatActivity.MODE_PRIVATE).getString(Key.KEY_APP_ID, null);
    var indexString="TopTopFiles${idResult}/";
    var  startPoint=this.indexOf(indexString)+indexString.length
    var result="";
    var fileName= this.substring(startPoint)
   var listData= fileName.split("_ID_")
    if(listData.isNotEmpty()){
        result= listData[0];
    }
    return result
}

fun String.getAudioEpFromUri(activity: Activity): Int {
    var idResult= activity.getPreferences(AppCompatActivity.MODE_PRIVATE).getString(Key.KEY_APP_ID, null);
    var indexString="TopTopFiles${idResult}/";
    var  startPoint=this.indexOf(indexString)+indexString.length
    var result=-1;
    try {
        var fileName= this.substring(startPoint)
        fileName = fileName.substring(0,fileName.length-4)
        var listData= fileName.split("_ID_")
        result= listData[1].toInt();
    }
    catch (e:Exception){

    }
    return result
}