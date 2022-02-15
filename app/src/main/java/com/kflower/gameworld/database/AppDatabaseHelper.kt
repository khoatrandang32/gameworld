package com.kflower.gameworld.database

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import android.util.Log


class AppDatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        UserTable(db).createTable();
        AudioTable(db).createTable();
        HomeCateTable(db).createTable();
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        UserTable(db).deleteTable();
        onCreate(db)
    }

    companion object {
        private const val TAG = "SQLite"

        // Database Version
        private const val DATABASE_VERSION = 1

        // Database Name
        private const val DATABASE_NAME = "GameWorld.db"

    }
}