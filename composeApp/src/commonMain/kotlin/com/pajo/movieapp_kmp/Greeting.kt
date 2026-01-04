package com.pajo.movieapp_kmp

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name} Payel!"
    }
}