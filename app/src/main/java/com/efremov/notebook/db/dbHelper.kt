package com.efremov.notebook.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class dbHelper(context: Context): SQLiteOpenHelper(context, db.DATABASE_NAME, null, db.DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(db.CREATE_TABLE)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        database?.execSQL(db.DROP_TABLE)
        onCreate(database)
    }
}