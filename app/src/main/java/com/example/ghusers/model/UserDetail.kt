package com.example.ghusers.model

import com.google.gson.annotations.SerializedName

data class UserDetail (
    val id: Int?,
    val name: String?,
    val login: String?,
    val bio: String?,
    val blog: String?,
    val followers: Int?,
    val following: Int?,
    @SerializedName("public_repos")
    val publicRepos: Int?,
    val location: String?,
    @SerializedName("twitter_username")
    val twiitter: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @SerializedName("avatar_url")
    val avatar: String?,
    val company: String?
)