package com.example.quanlychungcu.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class User(
    val id: Int,
    val username: String,
    val email: String,
    @SerialName("role_id") val roleId: Int,
    @SerialName("role_name") val roleName: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val staff: Staff? = null,
    val resident: Resident? = null
)
