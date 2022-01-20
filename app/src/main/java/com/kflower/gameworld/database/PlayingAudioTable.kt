package com.kflower.gameworld.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kflower.gameworld.User
import com.kflower.gameworld.model.AudioBook

class AudioTable(db: SQLiteDatabase?) {

    private var db: SQLiteDatabase? = db
    private val TABLE_AUDIO = "TABLE_AUDIO"
    private val AUDIO_ID = "AUDIO_ID"
    private val AUDIO_TITLE = "AUDIO_TITLE"
    private val AUDIO_IMG = "AUDIO_IMG"
    private val AUDIO_EPISODES = "AUDIO_EPISODES"
    private val AUDIO_AURHOR = "AUDIO_AURHOR"
    private val AUDIO_READER = "AUDIO_READER"
    private val AUDIO_RATE = "AUDIO_RATE"
    private val AUDIO_PROGRESS = "AUDIO_PROGRESS"
    private val AUDIO_CUR_EP = "AUDIO_CUR_EP"

    fun createTable() {
        val script = ("CREATE TABLE ${TABLE_AUDIO}( " +
                "${AUDIO_ID} TEXT PRIMARY KEY ," +
                "${AUDIO_TITLE} TEXT," +
                "${AUDIO_IMG} TEXT," +
                "${AUDIO_EPISODES} TEXT," +
                "${AUDIO_AURHOR} TEXT," +
                "${AUDIO_READER} TEXT," +
                "${AUDIO_RATE} TEXT," +
                "${AUDIO_PROGRESS} TEXT," +
                "${AUDIO_CUR_EP} TEXT" + ")")
        db?.execSQL(script)
    }

    fun deleteTable() {
        val script = ("DROP TABLE IF EXISTS ${TABLE_AUDIO}")
        db?.execSQL(script)
    }

    fun addNewUser(context: Context?, audio: AudioBook): Long? {
        val dbHelper = AppDatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(AUDIO_ID, audio.id)
            put(AUDIO_TITLE, audio.title)
        }
        val newRowId = db?.insert(TABLE_AUDIO, null, values)
        return newRowId;
    }

    fun findUser(context: Context?, username: String?): MutableList<User> {
        val dbHelper = AppDatabaseHelper(context)
        val listUser = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_AUDIO,
            arrayOf(
                AUDIO_ID,
                AUDIO_TITLE,
                AUDIO_AURHOR
            ),
            AUDIO_TITLE + "=?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
        with(cursor) {
//            while (moveToNext()) {
//                listUser.add(
//                    User(
//                        cursor?.getString(1),
//                        cursor?.getString(2)
//                    )
//                )
//            }
        }
        cursor.close()
        return listUser
    }
}

