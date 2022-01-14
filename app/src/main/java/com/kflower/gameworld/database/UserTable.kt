package com.kflower.gameworld.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.kflower.gameworld.User

class UserTable(db: SQLiteDatabase?) {

    private var db: SQLiteDatabase? = db
    private val TABLE_USER = "TABLE_USER"
    private val USER_ID = "USER_ID"
    private val USERNAME = "USERNAME"
    private val PASSWORD = "PASSWORD"

    fun createTable() {
        val script = ("CREATE TABLE ${TABLE_USER}( " +
                "${USER_ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "${USERNAME} TEXT," +
                "${PASSWORD} TEXT" + ")")
        db?.execSQL(script)
    }

    fun deleteTable() {
        val script = ("DROP TABLE IF EXISTS ${TABLE_USER}")
        db?.execSQL(script)
    }

    fun addNewUser(context: Context?, user: User): Long? {
        val dbHelper = AppDatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(USERNAME, user.email)
            put(PASSWORD, user.password)
        }
        val newRowId = db?.insert(TABLE_USER, null, values)
        return newRowId;
    }

    fun addNote(context: Context,user: User) {
        val dbHelper = AppDatabaseHelper(context)
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(USERNAME, user.email)
        values.put(PASSWORD, user.password)

        db.insert(TABLE_USER, null, values)

        db.close()
    }

    fun findUser(context: Context?, username: String?):MutableList<User> {
        val dbHelper = AppDatabaseHelper(context)
        val listUser = mutableListOf<User>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_USER,
            arrayOf(
                USER_ID,
                USERNAME,
                PASSWORD
            ),
            USERNAME + "=?",
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