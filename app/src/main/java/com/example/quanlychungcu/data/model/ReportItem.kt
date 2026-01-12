package com.example.quanlychungcu.data.model

import androidx.compose.ui.graphics.ImageBitmap

data class ReportItem(
    val id: String,
    val title: String,
    val content: String,
    val status: String, // "pending", "processed"
    val date: String,
    val time: String,
    val type: String,
    val images: List<String> = emptyList(),
    val capturedImage: ImageBitmap? = null // Dòng này bắt buộc để lưu ảnh chụp
)