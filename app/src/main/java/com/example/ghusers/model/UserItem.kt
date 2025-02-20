package com.example.ghusers.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val id: Int?,
    val login: String?,
    @SerializedName("avatar_url")
    val avatarUrl: String?
): Parcelable