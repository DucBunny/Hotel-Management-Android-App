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
      }
      else {
        val errorResponse = try {
          // Giả sử server trả về JSON có trường error, hoặc bạn đọc thô chuỗi String
          response.body<Map<String, String>>()["error"]
            ?: response.body<String>()
        } catch (e: Exception) {
          android.util.Log.e(
            "AuthService",
            "Lỗi khi đọc body lỗi từ server: ${e.message}",
            e
          )
          "Lỗi không xác định (${response.status.value})"
        }
        // Ném ngoại lệ với nội dung lỗi từ server
        throw Exception(if (errorResponse == "Invalid login credentials") "Tài khoản hoặc mật khẩu không đúng" else errorResponse)
      }
    } catch (e: Exception) {
      if (e.message?.contains("timeout") == true || e.message?.contains("Connect") == true) {
        throw Exception("Không thể kết nối đến máy chủ. Vui lòng kiểm tra mạng!")
      }
      throw e
    }
  }
}