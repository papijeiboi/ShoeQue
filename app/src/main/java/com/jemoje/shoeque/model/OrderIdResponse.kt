package com.jemoje.shoeque.model

import com.google.gson.annotations.SerializedName

class OrderIdResponse {

    @SerializedName("sale_id")
    var saleId: String? = null

    @SerializedName("stock_id")
    var stockId: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("id")
    var id: String? = null
}