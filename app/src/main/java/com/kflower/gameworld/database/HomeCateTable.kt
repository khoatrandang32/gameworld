package com.kflower.gameworld.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.kflower.gameworld.model.AudioBook
import com.kflower.gameworld.model.AudioGroup
import com.kflower.gameworld.model.Category


class HomeCateTable(var db: SQLiteDatabase) {

    private val TABLE_HOMECATE = "TABLE_HOMECATE"
    private val HOMECATE_ID = "HOMECATE_ID"
    private val HOMECATE_TITLE = "HOMECATE_TITLE"


    fun createTable() {
        val script = ("CREATE TABLE ${TABLE_HOMECATE}( " +
                "${HOMECATE_ID} TEXT PRIMARY KEY ," +
                "${HOMECATE_TITLE} TEXT)")
        db.execSQL(script)
    }

    fun deleteTable() {
        val script = ("DROP TABLE IF EXISTS ${TABLE_HOMECATE}")
        db.execSQL(script)
    }

    fun addNewAudioGroup(audioGroup: AudioGroup){
        val values = ContentValues().apply {
            put(HOMECATE_ID, audioGroup.id)
            put(HOMECATE_TITLE, audioGroup.title)

        }
        if(findAudioGroup(audioGroup.id).size<=0){

            db.insert(TABLE_HOMECATE, null, values);
        }
        else{
            db.update(TABLE_HOMECATE, values, "$HOMECATE_ID = ?", arrayOf(audioGroup.id));

        }


    }

    fun findAudioGroup(id: String?): MutableList<AudioGroup> {
        val listUser = mutableListOf<AudioGroup>()
        val cursor: Cursor = db.query(
            TABLE_HOMECATE,
            arrayOf(
                HOMECATE_ID,
                HOMECATE_TITLE,

            ),
            HOMECATE_ID + "=?",
            arrayOf(id),
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (moveToNext()) {
                listUser.add(
                    AudioGroup(
                        id = cursor?.getString(1),
                        title = cursor?.getString(2),
                        listAudio = mutableListOf()
                    )
                )
            }
        }
        cursor.close()
        return listUser
    }
}
