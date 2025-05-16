package org.example.miniproyecto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform