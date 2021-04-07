package com.reza.submission2bfaa.db

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class NoteColumns:BaseColumns{
        companion object {
            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME_DB = "usesrname"
            const val PHOTO_DB = "photo"
            const val FAVOURITE_DB = 0
        }
    }
}