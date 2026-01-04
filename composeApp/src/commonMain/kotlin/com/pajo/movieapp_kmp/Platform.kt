package com.pajo.movieapp_kmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform