package com.jemoje.shoeque.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShoesData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("qr_code")
    var qrCode: String? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

    @SerializedName("sizes")
    @Expose
    var sizes: MutableList<SizesData>? = null

    @SerializedName("images")
    @Expose
    var images: MutableList<ImagesData>? = null

}