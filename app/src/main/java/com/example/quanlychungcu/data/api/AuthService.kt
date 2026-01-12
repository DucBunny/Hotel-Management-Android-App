package com.example.quanlychungcu.data.api

import com.example.quanlychungcu.data.model.LoginRequest
import com.example.quanlychungcu.data.model.LoginResponse
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

object AuthService {
    private val client = KtorClient.httpClient

    // Hàm suspend (Async)
    suspend fun login(username: String, password: String): LoginResponse? {
        return try {
            val response = client.post(KtorClient.getUrl("/login")) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(username, password))
            }

            if (response.status == HttpStatusCode.OK) {
                response.body() // Return data
            } else {
                val errorBody = runCatching { response.body<String>() }.getOrNull()
                throw Exception(
                    "Login failed: ${response.status.value} ${response.status.description}" +
                            (if (!errorBody.isNullOrBlank()) " - $errorBody" else "")
                )
            }
        } catch (e: Exception) {
            throw Exception("Login request error: ${e.message}", e)
        }
    }
}