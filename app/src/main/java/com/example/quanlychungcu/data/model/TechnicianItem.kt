package com.example.quanlychungcu.data.model

import androidx.compose.ui.graphics.vector.ImageVector

// Dữ liệu một yêu cầu sửa chữa
data class RequestItem(
    val id: String,
    val title: String,
    val reporter: String,
    val apartment: String,
    val type: String,
    val description: String,
    val priority: String, // high, medium, low
    val status: String,   // accepted, processing, completed, cancelled
    val time: String,
    val technician: String,
    val completedAt: String? = null,
    val images: List<String> = emptyList(),
    val history: List<Map<String, String>> = emptyList()
)

// Dữ liệu báo cáo hoàn thành công việc
data class CompletionReport(
    var description: String = "",
    var cause: String = "",
    var note: String = "",
    var images: List<String> = emptyList()
)

// Menu Item cho Sidebar Kỹ thuật
data class TechMenuItem(
    val icon: ImageVector,
    val label: String,
    val active: Boolean = false
)

data class TechMenuGroup(
    val title: String,
    val items: List<TechMenuItem>
)