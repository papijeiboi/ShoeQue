package com.jemoje.shoeque.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {

    @SerializedName("token")
    @Expose
    var token: TokenResponse? = null

    @SerializedName("user")
    @Expose
    var user: UserData? = null
}