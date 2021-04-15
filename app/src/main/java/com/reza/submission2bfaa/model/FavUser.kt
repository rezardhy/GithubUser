package com.reza.submission2bfaa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavUser (
    val id :Int = 0,
    val username:String?=null,
    val photo:String?=null,
    val fav:String?=null
): Parcelable