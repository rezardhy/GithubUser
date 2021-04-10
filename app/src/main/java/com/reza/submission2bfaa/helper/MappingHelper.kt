package com.reza.submission2bfaa.helper

import android.database.Cursor
import com.reza.submission2bfaa.db.DatabaseContract
import com.reza.submission2bfaa.model.FavUser

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<FavUser> {
        val userList = ArrayList<FavUser>()
        userCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.NoteColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME_DB))
                val photo = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.PHOTO_DB))
                val favourite = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FAVOURITE_DB))
                userList.add(FavUser(id,username, photo, favourite))
            }
        }
        return userList
    }

}