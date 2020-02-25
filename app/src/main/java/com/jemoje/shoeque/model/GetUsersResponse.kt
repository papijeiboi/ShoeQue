package com.jemoje.shoeque.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetUsersResponse {

    @SerializedName("current_page")
    var currentPage: String? = null

    @SerializedName("data")
    @Expose
    var data: MutableList<UserData>? = null

    @SerializedName("first_page_url")
    var firstPageUrl: String? = null

    @SerializedName("from")
    var from: String? = null

    @SerializedName("last_page")
    var lastPage: String? = null

    @SerializedName("last_page_url")
    var lastPageUrl: String? = null

    @SerializedName("next_page_url")
    var nextPageUrl: String? = null

    @SerializedName("path")
    var path: String? = null

    @SerializedName("per_page")
    var perPage: String? = null

    @SerializedName("prev_page_url")
    var prevPageUrl: String? = null

    @SerializedName("to")
    var to: String? = null

    @SerializedName("total")
    var total: String? = null
}