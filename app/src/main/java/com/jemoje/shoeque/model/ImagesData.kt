package com.jemoje.shoeque.model

import com.google.gson.annotations.SerializedName

class ImagesData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("image")
    var image: String? = null

    @SerializedName("image_url")
    var imageUrl: String? = null

    @SerializedName("imageable_id")
    var imageableId: String? = null

    @SerializedName("imageable_type")
    var imageableType: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null
}