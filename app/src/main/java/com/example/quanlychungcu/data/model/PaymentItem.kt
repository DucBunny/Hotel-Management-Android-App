package com.example.quanlychungcu.data.model

// Chi tiết từng dịch vụ trong hóa đơn (Điện, nước, gửi xe...)
data class ServiceDetailItem(
    val name: String,
    val price: String,
    val qty: String = "",
    val total: String = ""
)

// Hóa đơn chưa thanh toán
data class UnpaidBill(
    val id: String,
    val title: String,
    val period: String,
    val amount: String,
    val dueDate: String,
    val details: List<ServiceDetailItem>
)

// Lịch sử thanh toán
data class PaymentHistoryItem(
    val id: String,
    val month: String,
    val billName: String,
    val amount: String,
    val time: String,
    val paymentDate: String,
    val dueDate: String,
    val status: String
)