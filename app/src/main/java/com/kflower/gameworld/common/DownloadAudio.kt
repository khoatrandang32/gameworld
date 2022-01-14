package com.kflower.gameworld.common

import android.app.Activity
import android.os.Environment

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URL
import java.net.URLConnection
import android.app.ProgressDialog
import android.content.Context


class DownloadAudio() :
    AsyncTask<String?, String?, String?>() {

    /**
     * Before starting background thread Show Progress Bar Dialog
     */
    override fun onPreExecute() {
        super.onPreExecute()
    }

    /**
     * Downloading file in background thread
     */
     override fun doInBackground(vararg f_url: String?): String? {
        var count: Int
        try {
            val url = URL(f_url[0])
            val connection: URLConnection = url.openConnection()
            connection.connect()

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            val lenghtOfFile: Int = connection.contentLength

            // download the file
            val input: InputStream = BufferedInputStream(
                url.openStream(),
                8192
            )

            // Output stream
            val output: OutputStream = FileOutputStream(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath +"/Kflower/"

            )
            val data = ByteArray(1024)
            var total: Long = 0
            while ((input.read(data).also { count = it }) != -1) {
                total += count.toLong()
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + ((total * 100) / lenghtOfFile).toInt())

                // writing data to file
                output.write(data, 0, count)
            }

            // flushing output
            output.flush()

            // closing streams
            output.close()
            input.close()
        } catch (e: Exception) {
            Log.e("Error: ", e.message.toString())
        }
        return null
    }

    override fun onProgressUpdate(vararg values: String?) {
         Log.d("KHOA", "onProgressUpdate: ${Integer.parseInt(values[0])}")
    }


    override fun onPostExecute(file_url: String?) {
        Log.d("KHOA", "onPostExecute: ")

    }
}