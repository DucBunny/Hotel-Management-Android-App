package com.example.quanlychungcu.data.model

data class UserProfile(
    var name: String,
    val role: String,
    val apartment: String,
    val joinDate: String,
    // Chi tiết
    var fullName: String,
    var email: String,
    var phone: String,
    var gender: String,
    var idCard: String,
    var dob: String,
    var hometown: String,
    var ethnicity: String,
    var job: String,
    var householdBook: String
)