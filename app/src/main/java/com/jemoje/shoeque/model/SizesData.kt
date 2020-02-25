package com.jemoje.shoeque.model

import com.google.gson.annotations.SerializedName

class SizesData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("size")
    var size: String? = null

    @SerializedName("quantity")
    var quantity: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updatedAt")
    var updatedAt: String? = null

}