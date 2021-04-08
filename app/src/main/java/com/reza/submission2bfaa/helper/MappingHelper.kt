package com.reza.submission2bfaa.helper

import android.database.Cursor
import com.reza.submission2bfaa.db.DatabaseContract
import com.reza.submission2bfaa.model.User

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<User> {
        val notesList = ArrayList<User>()
        userCursor?.apply {
            while (moveToNext()) {
                val username = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME_DB))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.PHOTO_DB))
                val favourite = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FAVOURITE_DB))
                notesList.add(User(username, photo, favourite))
            }
        }
        return notesList
    }

}