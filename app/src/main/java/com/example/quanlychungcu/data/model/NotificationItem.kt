package com.example.quanlychungcu.data.model

data class NotificationItem(
    val id: Int,
    val title: String,
    val category: String, // "Hóa đơn", "Hệ thống", "Sự kiện", v.v.
    val color: String,    // "blue", "green", "orange", "purple", "gray"
    val content: String,
    val time: String,
    val date: String,
    val isRead: Boolean
)