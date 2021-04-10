package com.reza.submission2bfaa.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.USERNAME_DB
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion._ID
import java.sql.SQLException

class UserHelper(context: Context) {

    init {
        dataBaseHelper = DatabaseHelper(context)
    }

    companion object{

        private const val DATABASE_TABLE = TABLE_NAME
        private lateinit var dataBaseHelper: DatabaseHelper
        private var INSTANCE: UserHelper? = null
        private lateinit var database: SQLiteDatabase

        fun getInstance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }


    }


    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }
    fun close() {
        dataBaseHelper.close()
        if (database.isOpen)
            database.close()
    }

    fun queryById(id: String): Cursor {
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME_DB = ?",
                arrayOf(id),
                null,
                null,
                null,
                null)
    }

    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC")
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteById(username: String): Int {
        return database.delete(DATABASE_TABLE, "$USERNAME_DB = '$username'", null)
    }

}