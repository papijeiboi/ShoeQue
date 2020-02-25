package com.jemoje.shoeque.constant

class Keys {

    companion object {
        lateinit var instance: Keys

        val USER_TOKEN = "user_token"
        val USER_FULL_DATA = "user_full_data"
    }

    init {
        instance = this
    }
}