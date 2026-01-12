package com.example.quanlychungcu.data.api

import com.example.quanlychungcu.BuildConfig // Import cái này để dùng biến môi trường
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object KtorClient {
    // Lấy URL từ local.properties
    private const val BASE_API_URL = BuildConfig.BASE_API_URL + "/api-v1"

    val httpClient = HttpClient(CIO) {
        // 1. Config JSON
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true // Server trả thừa trường cũng ko lỗi
                prettyPrint = true
                isLenient = true
            })
        }

        // 2. Config Log (Để soi request/response ở Logcat)
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL // Hiện full header, body
        }

        // 3. Config Timeout (tránh treo app)
        engine {
            requestTimeout = 10000 // 10 giây
        }
    }

    // Helper function để nối chuỗi URL
    fun getUrl(path: String): String {
        return "$BASE_API_URL$path"
    }
}