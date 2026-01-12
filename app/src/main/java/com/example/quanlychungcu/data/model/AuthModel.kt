package com.example.quanlychungcu.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(
  val message: String,
  val user: User,
  val token: String
)