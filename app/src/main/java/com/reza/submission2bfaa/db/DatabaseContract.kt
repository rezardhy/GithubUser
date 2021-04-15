package com.reza.submission2bfaa.db

import android.net.Uri
import android.provider.BaseColumns

internal class DatabaseContract {
    internal class NoteColumns:BaseColumns{
        companion object {

            const val AUTHORITY = "com.reza.submission2bfaa"
            const val SCHEME = "content"

            const val TABLE_NAME = "user"
            const val _ID = "_id"
            const val USERNAME_DB = "username"
            const val PHOTO_DB = "photo"
            const val FAVOURITE_DB = "favourite"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NAME)
                    .build()

        }
    }
}