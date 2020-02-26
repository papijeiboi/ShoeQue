package com.jemoje.shoeque.model

import com.google.gson.annotations.SerializedName

class OrderProductResponse {

    @SerializedName("order_id")
    var orderId: String? = null

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("size_id")
    var sizeId: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: String? = null
}