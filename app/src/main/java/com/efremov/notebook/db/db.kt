package com.efremov.notebook.db

import android.provider.BaseColumns

object db: BaseColumns {
    const val TABLE_NAME = "notebook"
    const val KEY_ID = "_id"
    const val COLUMN_NAME = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_COLOR = "color"
    const val COLUMN_NAME_STATUS = "status"

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "notes.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "$KEY_ID INTEGER PRIMARY KEY," +
            "$COLUMN_NAME TEXT," +
            "$COLUMN_NAME_CONTENT TEXT," +
            "$COLUMN_NAME_COLOR TEXT," +
            "$COLUMN_NAME_STATUS TEXT)"

    const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}