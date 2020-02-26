package com.jemoje.shoeque.webservice

import com.google.gson.GsonBuilder
import com.jemoje.shoeque.model.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface UserService {

    @FormUrlEncoded
    @POST("/api/login")
    fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): retrofit2.Call<UserResponse>

    @GET("/api/shoes/accounts")
    fun getUsers(
        @Query("type") type: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<GetUsersResponse>

    @FormUrlEncoded
    @POST("/api/shoes/accounts")
    fun addUsers(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("user_type") userType: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<UserData>

    @GET("/api/shoes/accounts/{id}")
    fun getUserInformation(
        @Path("id") id: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<UserData>

    @FormUrlEncoded
    @POST("/api/shoes/accounts/{id}")
    fun updateUserInformationWithPassword(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("id") userId: String,
        @Field("user_type") userType: String,
        @Field("_method") method: String,
        @Path("id") id: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<UserData>

    @FormUrlEncoded
    @POST("/api/shoes/accounts/{id}")
    fun updateUserInformation(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("id") userId: String,
        @Field("user_type") userType: String,
        @Field("_method") method: String,
        @Path("id") id: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<UserData>

    @DELETE("/api/shoes/accounts/{id}")
    fun deleteUser(
        @Path("id") id: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<DeleteResponse>

    @GET("/api/shoes/products")
    fun displayShoes(
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<DisplayShoesResponse>

    @GET("/api/shoes/products")
    fun scanQr(
        @Query("qr_code") qrCode: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<ShoesData>

    @GET("/api/shoes/products")
    fun recommendation(
        @Query("search") search: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<DisplayShoesResponse>

    @FormUrlEncoded
    @POST("/api/shoes/orders")
    fun createOrderId(
        @Field("sale_id") saleId: String,
        @Field("stock_id") stockId: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<OrderIdResponse>

    @FormUrlEncoded
    @POST("/api/shoes/ordered-products")
    fun orderProduct(
        @Field("order_id") orderId: String,
        @Field("product_id") productId: String,
        @Field("size_id") sizeId: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<OrderProductResponse>

    @GET("/api/shoes/ordered-products")
    fun displayOrders(
        @Query("sort_by") sortBy: String,
        @Query("sort_order") sortOrder: String,
        @Header("Accept") accept: String,
        @Header("Authorization") authHeader: String
    ): retrofit2.Call<DisplayOrdersResponse>


    companion object Factory {
        var gson = GsonBuilder()
            .setLenient()
            .create()

        fun create(baseUrl: String): UserService {
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit.create(UserService::class.java)
        }
    }
}