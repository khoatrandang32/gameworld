package com.kflower.gameworld.database

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.kflower.gameworld.model.AudioBook
import com.google.gson.Gson
import com.kflower.gameworld.model.Category
import com.google.gson.reflect.TypeToken
import com.kflower.gameworld.MyApplication.Companion.TAG
import com.kflower.gameworld.MyApplication.Companion.mAppContext
import com.kflower.gameworld.common.Key
import java.lang.reflect.Type

class AudioTable(var db: SQLiteDatabase) {

    private val TABLE_AUDIO = "TABLE_AUDIO"
    private val AUDIO_ID = "AUDIO_ID"
    private val AUDIO_TITLE = "AUDIO_TITLE"
    private val AUDIO_IMG = "AUDIO_IMG"
    private val AUDIO_BASE_EPISODE = "AUDIO_BASE_EPISODE"
    private val AUDIO_AURHOR = "AUDIO_AURHOR"
    private val AUDIO_READER = "AUDIO_READER"
    private val AUDIO_RATE = "AUDIO_RATE"
    private val AUDIO_PROGRESS = "AUDIO_PROGRESS"
    private val AUDIO_CUR_EP = "AUDIO_CUR_EP"
    private val AUDIO_DESCRIPTION = "AUDIO_DESCRIPTION"
    private val AUDIO_EP_AMOUNT = "AUDIO_EP_AMOUNT"
    private val AUDIO_CATEGORIES = "AUDIO_CATEGORIES"
    private val AUDIO_THUMBNAIL = "AUDIO_THUMBNAIL"
    private val AUDIO_HISTORY_TIME = "AUDIO_HISTORY_TIME"

    fun createTable() {
        val script = ("CREATE TABLE ${TABLE_AUDIO}( " +
                "${AUDIO_ID} TEXT PRIMARY KEY ," +
                "${AUDIO_TITLE} TEXT," +
                "${AUDIO_IMG} TEXT," +
                "${AUDIO_BASE_EPISODE} TEXT," +
                "${AUDIO_AURHOR} TEXT," +
                "${AUDIO_READER} TEXT," +
                "${AUDIO_RATE} TEXT," +
                "${AUDIO_DESCRIPTION} TEXT," +
                "${AUDIO_THUMBNAIL} TEXT," +
                "${AUDIO_CATEGORIES} TEXT," +
                "${AUDIO_PROGRESS} INTEGER," +
                "${AUDIO_EP_AMOUNT} INT," +
                "${AUDIO_CUR_EP} INT," +
                "${AUDIO_HISTORY_TIME} INT" +
                ")")
        db.execSQL(script)
    }

    fun deleteTable() {
        val script = ("DROP TABLE IF EXISTS ${TABLE_AUDIO}")
        db.execSQL(script)
    }

    fun addNewAudio(audio: AudioBook) {
        val gson = Gson()
        val categoriesJsonStr = gson.toJson(audio.categories)

        if (findAudio(audio.id).size <= 0) {
            val values = ContentValues().apply {
                put(AUDIO_ID, audio.id)
                put(AUDIO_TITLE, audio.title)
                put(AUDIO_IMG, audio.imgBase64)
                put(AUDIO_AURHOR, audio.author)
                put(AUDIO_READER, audio.reader)
                put(AUDIO_PROGRESS, audio.progress)
                put(AUDIO_CUR_EP, audio.curEp)
                put(AUDIO_BASE_EPISODE, audio.baseEpisode)
                put(AUDIO_DESCRIPTION, audio.decription)
                put(AUDIO_EP_AMOUNT, audio.episodesAmount)
                put(AUDIO_THUMBNAIL, audio.thumbnailUrl)
                put(AUDIO_CATEGORIES, categoriesJsonStr)
                put(AUDIO_RATE, audio.rate)
                put(AUDIO_HISTORY_TIME, audio.historyTime)
            }
            db.insert(TABLE_AUDIO, null, values);
        } else {
            val values = ContentValues().apply {
                put(AUDIO_ID, audio.id)
                put(AUDIO_TITLE, audio.title)
                put(AUDIO_IMG, audio.imgBase64)
                put(AUDIO_AURHOR, audio.author)
                put(AUDIO_READER, audio.reader)
                put(AUDIO_BASE_EPISODE, audio.baseEpisode)
                put(AUDIO_DESCRIPTION, audio.decription)
                put(AUDIO_EP_AMOUNT, audio.episodesAmount)
                put(AUDIO_CATEGORIES, categoriesJsonStr)
                put(AUDIO_RATE, audio.rate)
            }
            db.update(TABLE_AUDIO, values, "$AUDIO_ID = ?", arrayOf(audio.id));
        }
    }

    fun findAudio(id: String?): MutableList<AudioBook> {
        val listUser = mutableListOf<AudioBook>()
        val cursor: Cursor = db.query(
            TABLE_AUDIO,
            arrayOf(
                AUDIO_ID,
                AUDIO_TITLE,
                AUDIO_IMG,
                AUDIO_AURHOR,
                AUDIO_READER,
                AUDIO_PROGRESS,
                AUDIO_CUR_EP,
                AUDIO_BASE_EPISODE,
                AUDIO_DESCRIPTION,
                AUDIO_EP_AMOUNT,
                AUDIO_THUMBNAIL,
                AUDIO_CATEGORIES,
                AUDIO_RATE,
                AUDIO_HISTORY_TIME,
            ),
            AUDIO_ID + "=?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val listType: Type = object : TypeToken<MutableList<Category?>>() {}.type
                val listCategory: MutableList<Category> =
                    Gson().fromJson(cursor?.getString(11), listType)

                try {
                    listUser.add(
                        AudioBook(
                            id = cursor?.getString(0),
                            title = cursor?.getString(1),
                            imgBase64 = cursor?.getString(2),
                            author = cursor?.getString(3),
                            reader = cursor?.getString(4),
                            progress = cursor?.getLong(5),
                            curEp = cursor?.getInt(6),
                            baseEpisode = cursor?.getString(7),
                            decription = cursor?.getString(8),
                            episodesAmount = cursor?.getInt(9),
                            thumbnailUrl = cursor?.getString(10),
                            categories = listCategory,
                            comments = null,
                            rate = cursor?.getInt(12),
                            historyTime = cursor?.getInt(13),

                            )
                    )
                } catch (error: Exception) {

                }
            }
        }
        cursor.close()
        return listUser
    }

    fun getAll(): MutableList<AudioBook> {
        val listUser = mutableListOf<AudioBook>()
        val cursor: Cursor = db.query(
            TABLE_AUDIO,
            arrayOf(
                AUDIO_ID,
                AUDIO_TITLE,
                AUDIO_IMG,
                AUDIO_AURHOR,
                AUDIO_READER,
                AUDIO_PROGRESS,
                AUDIO_CUR_EP,
                AUDIO_BASE_EPISODE,
                AUDIO_DESCRIPTION,
                AUDIO_EP_AMOUNT,
                AUDIO_THUMBNAIL,
                AUDIO_CATEGORIES,
                AUDIO_RATE,
                AUDIO_HISTORY_TIME,
            ),
            null,
            null,
            null,
            null,
            "$AUDIO_HISTORY_TIME DESC",
            null
        )
        with(cursor) {
            while (moveToNext()) {
                val listType: Type = object : TypeToken<MutableList<Category?>>() {}.type
                val listCategory: MutableList<Category> =
                    Gson().fromJson(cursor?.getString(11), listType)

                try {
                    listUser.add(
                        AudioBook(
                            id = cursor?.getString(0),
                            title = cursor?.getString(1),
                            imgBase64 = cursor?.getString(2),
                            author = cursor?.getString(3),
                            reader = cursor?.getString(4),
                            progress = cursor?.getLong(5),
                            curEp = cursor?.getInt(6),
                            baseEpisode = cursor?.getString(7),
                            decription = cursor?.getString(8),
                            episodesAmount = cursor?.getInt(9),
                            thumbnailUrl = cursor?.getString(10),
                            categories = listCategory,
                            comments = null,
                            rate = cursor?.getInt(12),
                            historyTime = cursor?.getInt(13),

                            )
                    )
                } catch (error: Exception) {

                }
            }
        }
        cursor.close()
        return listUser
    }


    fun updateAudioProgress(audio: AudioBook) {
        val values = ContentValues().apply {
            put(AUDIO_PROGRESS, audio.progress)
        }
        db.update(TABLE_AUDIO, values, "$AUDIO_ID = ?", arrayOf(audio.id));
    }

    fun updateAudioHistoryTime(context: Context,audioId:String) {

        val sharedPref: SharedPreferences = context.getSharedPreferences(Key.KEY_STORE, Context.MODE_PRIVATE)
        //
        val listAudioGroupsJson = sharedPref.getInt(Key.KEY_AUDIO_HISTORY_ID, 1)

        val editor = sharedPref.edit()
        editor.putInt(Key.KEY_AUDIO_HISTORY_ID, listAudioGroupsJson+1)
        editor.commit()

        val values = ContentValues().apply {
            put(AUDIO_HISTORY_TIME, listAudioGroupsJson)
        }
        db.update(TABLE_AUDIO, values, "$AUDIO_ID = ?", arrayOf(audioId));
    }

    fun updateAudioEp(audio: AudioBook) {
        val values = ContentValues().apply {
            put(AUDIO_CUR_EP, audio.curEp)
            put(AUDIO_PROGRESS, 0)
        }
        db.update(TABLE_AUDIO, values, "$AUDIO_ID = ?", arrayOf(audio.id));
    }
}

