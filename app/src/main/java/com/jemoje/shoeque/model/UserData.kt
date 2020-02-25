package com.jemoje.shoeque.model

import com.google.gson.annotations.SerializedName

class UserData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("email_verified_at")
    var emailVerifiedAt: String? = null

    @SerializedName("user_type")
    var userType: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null

}