package com.example.quanlychungcu.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Resident(
    val id: Int,
    @SerialName("user_id") val userId: Int,
    @SerialName("full_name") val fullName: String,
    val dob: String,
    val gender: Int,
    val phone: String,
    @SerialName("place_of_birth") val placeOfBirth: String,
    val ethnicity: String,
    val occupation: String,
    val hometown: String,
    @SerialName("id_card") val idCard: String,
    @SerialName("household_no") val householdNo: String,
    val status: Int,
    @SerialName("registered_at") val registeredAt: String,
)
