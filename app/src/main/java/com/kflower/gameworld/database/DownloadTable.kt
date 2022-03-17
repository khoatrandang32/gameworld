package com.kflower.gameworld.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.kflower.gameworld.enum.DownloadState
import com.kflower.gameworld.model.DownloadAudio
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.*
import java.io.File


class DownloadTable(var db: SQLiteDatabase) {

    private val TABLE_DOWNLOAD = "TABLE_DOWNLOAD"
    private val DOWNLOAD_ID = "DOWNLOAD_ID"
    private val DOWNLOAD_AUDIO_ID = "DOWNLOAD_AUDIO_ID"
    private val DOWNLOAD_URI = "DOWNLOAD_URI"
    private val DOWNLOAD_EP = "DOWNLOAD_EP"
    private val DOWNLOAD_PROGRESS = "DOWNLOAD_PROGRESS"
    private val DOWNLOAD_STATE = "DOWNLOAD_STATE"


    fun createTable() {
        val script = ("CREATE TABLE ${TABLE_DOWNLOAD}( " +
                "${DOWNLOAD_ID} TEXT PRIMARY KEY ," +
                "${DOWNLOAD_AUDIO_ID} TEXT ," +
                "${DOWNLOAD_URI} TEXT ," +
                "${DOWNLOAD_STATE} TEXT ," +
                "${DOWNLOAD_PROGRESS} INT ," +
                "${DOWNLOAD_EP} INT)")
        db.execSQL(script)
    }

    fun deleteTable() {
        val script = ("DROP TABLE IF EXISTS ${TABLE_DOWNLOAD}")
        db.execSQL(script)
    }

    fun updateDownloadProgress(download: DownloadAudio) {
        val values = ContentValues().apply {
            put(DOWNLOAD_PROGRESS, download.progress)
        }
        db.update(TABLE_DOWNLOAD, values, "$DOWNLOAD_ID = ?", arrayOf(download.id));
    }

    fun updateDownloadState(id: String, state: DownloadState) {
        val values = ContentValues().apply {
            put(DOWNLOAD_STATE, state.name)
        }
        db.update(TABLE_DOWNLOAD, values, "$DOWNLOAD_ID = ?", arrayOf(id));
    }


    fun addNewDownload(download: DownloadAudio) {
        val values = ContentValues().apply {
            put(DOWNLOAD_ID, download.id)
            put(DOWNLOAD_AUDIO_ID, download.audioId)
            put(DOWNLOAD_URI, download.uri)
            put(DOWNLOAD_STATE, download.state.name)
            put(DOWNLOAD_PROGRESS, download.progress)
            put(DOWNLOAD_EP, download.ep)

        }
        if (findDownload(download.id).size <= 0) {
            Log.d(TAG, "addNewDownload insert: "+download.id+" - "+download.audioId+" - "+download.ep)
            db.insert(TABLE_DOWNLOAD, null, values)
        } else {
            db.update(TABLE_DOWNLOAD, values, "$DOWNLOAD_ID = ?", arrayOf(download.id));

        }


    }

    fun deleteSpecificContents(id: String) {
        db.delete(TABLE_DOWNLOAD, "$DOWNLOAD_ID =?", arrayOf(id))
    }

    fun findDownload(id: String?): MutableList<DownloadAudio> {
        val listDownloads = mutableListOf<DownloadAudio>()
        val cursor: Cursor = db.query(
            TABLE_DOWNLOAD,
            arrayOf(
                DOWNLOAD_ID,
                DOWNLOAD_AUDIO_ID,
                DOWNLOAD_URI,
                DOWNLOAD_STATE,
                DOWNLOAD_PROGRESS,
                DOWNLOAD_EP,


                ),
            "$DOWNLOAD_ID =?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                listDownloads.add(
                    DownloadAudio(
                        id = cursor?.getString(0),
                        audioId = cursor?.getString(1),
                        uri = cursor?.getString(2),
                        state = DownloadState.valueOf(cursor?.getString(3)),
                        progress = cursor?.getInt(4),
                        ep = cursor?.getInt(5),
                    )
                )
            }
        }
        cursor.close()
        return listDownloads
    }

    fun findDownloadByAudioId(audioId: String?, audioEp: Int): DownloadAudio? {
        Log.d(TAG, "findDownloadByAudioId: $audioId : $audioEp")
        var result: DownloadAudio? = null
        val cursor: Cursor = db.query(
            TABLE_DOWNLOAD,
            arrayOf(
                DOWNLOAD_ID,
                DOWNLOAD_AUDIO_ID,
                DOWNLOAD_URI,
                DOWNLOAD_STATE,
                DOWNLOAD_PROGRESS,
                DOWNLOAD_EP,


                ),
            "$DOWNLOAD_AUDIO_ID =? AND $DOWNLOAD_EP =?",
            arrayOf(audioId, audioEp.toString()),
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                result = DownloadAudio(
                    id = cursor?.getString(0),
                    audioId = cursor?.getString(1),
                    uri = cursor?.getString(2),
                    state = DownloadState.valueOf(cursor?.getString(3)),
                    progress = cursor?.getInt(4),
                    ep = cursor?.getInt(5),
                )
            }
        }
        cursor.close()
        result?.apply {
            Log.d(TAG, "findDownloadByAudioId: "+uri)
            val file = File(uri)
            Log.d(TAG, "findDownloadByAudioId exists: "+file.exists())

            if (!file.exists()) {
                deleteSpecificContents(id)
                result= null
            }
        }
        Log.d(TAG, "findDownloadByAudioId: $result")
        return result
    }

    fun findDownloadsByState(state: DownloadState): MutableList<DownloadAudio> {
        val listDownloads = mutableListOf<DownloadAudio>()
        val cursor: Cursor = db.query(
            TABLE_DOWNLOAD,
            arrayOf(
                DOWNLOAD_ID,
                DOWNLOAD_AUDIO_ID,
                DOWNLOAD_URI,
                DOWNLOAD_STATE,
                DOWNLOAD_PROGRESS,
                DOWNLOAD_EP,


                ),
            "$DOWNLOAD_STATE =?",
            arrayOf(state.name),
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                listDownloads.add(
                    DownloadAudio(
                        id = cursor?.getString(0),
                        audioId = cursor?.getString(1),
                        uri = cursor?.getString(2),
                        state = DownloadState.valueOf(cursor?.getString(3)),
                        progress = cursor?.getInt(4),
                        ep = cursor?.getInt(5),
                    )
                )
            }
        }
        cursor.close()
        return listDownloads
    }

}
