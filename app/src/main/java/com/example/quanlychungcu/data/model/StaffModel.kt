package com.example.quanlychungcu.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Staff(
    val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("full_name") val fullName: String,
    val dob: String,
    val gender: Int,
    val phone: String,
    val position: String,
    val department: String,
    @SerialName("id_card") val idCard: String,
    @SerialName("start_date") val startDate: String,
    val status: Int
)
