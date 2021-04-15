package com.reza.submission2bfaa.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.AUTHORITY
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.reza.submission2bfaa.db.DatabaseContract.NoteColumns.Companion.USERNAME_DB
import com.reza.submission2bfaa.db.UserHelper

class MyContentProvider : ContentProvider() {

    companion object {
        private const val USERALL = 1
        private const val USERNAME= 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper
        init {
            // content:com.reza.submission2bfaa/user
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USERALL)
            // content://com.reza.submission2bfaa/user//username/username
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/$USERNAME_DB/*", USERNAME)
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USERALL -> userHelper.queryAll()
            USERNAME -> userHelper.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USERALL) {
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$added")    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USERNAME) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        return  0
    }
}