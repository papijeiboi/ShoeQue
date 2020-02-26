package com.jemoje.shoeque.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DisplayOrdersResponseData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("order_id")
    var orderId: String? = null

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("size_id")
    var sizeId: String? = null

    @SerializedName("created_at")
    var createdAt: String? = null

    @SerializedName("updated_at")
    var updatedAt: String? = null

    @SerializedName("deleted_at")
    var deletedAt: String? = null

    @SerializedName("order")
    @Expose
    var order: OrderIdResponse? = null

    @SerializedName("product")
    @Expose
    var product: ShoesData? = null

    @SerializedName("size")
    @Expose
    var size: SizesData? = null
}