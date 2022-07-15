package com.efremov.notebook.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.efremov.notebook.data.DataNote
import com.efremov.notebook.db.db.COLUMN_NAME
import com.efremov.notebook.db.db.COLUMN_NAME_COLOR
import com.efremov.notebook.db.db.COLUMN_NAME_CONTENT
import com.efremov.notebook.db.db.COLUMN_NAME_STATUS
import com.efremov.notebook.db.db.KEY_ID

class dbManager(context: Context) {
    private val dbHelper = dbHelper(context)
    var database: SQLiteDatabase? = null

    fun openDatabase() {
        database = dbHelper.writableDatabase
    }

    fun createDatabase() {
        database?.execSQL(db.CREATE_TABLE)
    }

    fun insertToDatabase(note: DataNote) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, note.name)
            put(COLUMN_NAME_CONTENT, note.content)
            put(COLUMN_NAME_COLOR, note.color)
            put(COLUMN_NAME_STATUS, note.status)
        }

        database?.insert(db.TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun readDatabase(): ArrayList<DataNote> {
        val data = ArrayList<DataNote>()
        val cursor = database?.query(db.TABLE_NAME, null, null, null, null, null, null)

        while(cursor?.moveToNext()!!) {
            val id = cursor.getString(cursor.getColumnIndex(KEY_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val content = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_CONTENT))
            val color = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_COLOR))
            val status = cursor.getString(cursor.getColumnIndex(COLUMN_NAME_STATUS))
            data.add(DataNote(id.toInt(), name, content, color, status))
        }

        cursor.close()
        return data
    }

    fun clearNotes() {
        database?.delete(db.TABLE_NAME, null, null)
        println("Dropped")
    }

    fun clearNote(note: DataNote) {
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, note.id)
        database?.delete(db.TABLE_NAME, KEY_ID + "=" + note.id, null)
    }

    fun updateNote(note: DataNote) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, note.name)
        contentValues.put(COLUMN_NAME_CONTENT, note.content)
        contentValues.put(COLUMN_NAME_COLOR, note.color)
        contentValues.put(COLUMN_NAME_STATUS, note.status)
        database?.update(db.TABLE_NAME, contentValues, KEY_ID + "=" + note.id, null)
    }

    fun closeDatabase() {
        dbHelper.close()
    }

    fun destroyDatabase() {
        database?.execSQL(db.DROP_TABLE)
    }

    fun getIndex(): Int {
        openDatabase()
        val array = readDatabase()
        var index = 1
        for(note in array) {
            if(note.status == "active") index += 1
        }

        return index
    }
}