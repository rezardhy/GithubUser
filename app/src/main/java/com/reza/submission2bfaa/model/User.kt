package com.reza.submission2bfaa.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
        var username:String="",
        var photo:String="",
        var name:String="",
        var company:String="",
        var location:String="",
        var repo :String=""



):Parcelable