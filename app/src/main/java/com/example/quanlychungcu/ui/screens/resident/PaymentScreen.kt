package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.data.model.PaymentHistoryItem
import com.example.quanlychungcu.data.model.ServiceDetailItem
import com.example.quanlychungcu.data.model.UnpaidBill
import com.example.quanlychungcu.ui.components.payment.*
import com.example.quanlychungcu.ui.theme.*

// Mock Data
val mockUnpaidBills = listOf(
    UnpaidBill(
        id = "BILL-202601",
        title = "Phí dịch vụ Tháng 1/2026",
        period = "Tháng 01",
        amount = "1.520.000 đ",
        dueDate = "31/01/2026",
        details = listOf(
            ServiceDetailItem("Phí quản lý", "850.000 đ"),
            ServiceDetailItem("Tiền nước (15m³)", "270.000 đ"),
            ServiceDetailItem("Gửi xe máy (x2)", "160.000 đ"),
            ServiceDetailItem("Gửi ô tô (x1)", "240.000 đ")
        )
    )
)

val mockHistoryData = listOf(
    PaymentHistoryItem("#HD-202512-01", "Tháng 12/2025", "Phí dịch vụ chung cư Tháng 12", "1.500.000 đ", "10:30:15", "05/01/2026", "10/01/2026", "Đã thanh toán"),
    PaymentHistoryItem("#HD-202511-01", "Tháng 11/2025", "Phí dịch vụ chung cư Tháng 11", "1.450.000 đ", "09:15:00", "05/12/2025", "10/12/2025", "Đã thanh toán")
)

val mockServiceDetailsFull = listOf(
    ServiceDetailItem("Phí quản lý", "12.000 đ", "85m²", "1.020.000 đ"),
    ServiceDetailItem("Tiền nước", "18.000 đ", "15m³", "270.000 đ"),
    ServiceDetailItem("Phí gửi xe máy", "80.000 đ", "2", "160.000 đ"),
    ServiceDetailItem("Phí vệ sinh", "50.000 đ", "1", "50.000 đ")
)

@Composable
fun PaymentScreen() {
    var selectedBill by remember { mutableStateOf<PaymentHistoryItem?>(null) }
    var unpaidBills by remember { mutableStateOf(mockUnpaidBills) }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        if (selectedBill != null) {
            BillDetailScreen(
                bill = selectedBill!!,
                onBack = { selectedBill = null }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Title
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Text("Thanh toán trực tuyến", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
                    Text("Thanh toán nhanh chóng và tiện lợi", style = MaterialTheme.typography.bodyMedium, color = TextGray, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Block 1
                PaymentCardSection(
                    title = "Hóa đơn chưa thanh toán",
                    icon = Icons.Filled.Receipt,
                    iconColor = if (unpaidBills.isNotEmpty()) RedTextSub else GreenText
                ) {
                    if (unpaidBills.isNotEmpty()) {
                        Column {
                            unpaidBills.forEach { bill ->
                                UnpaidBillCard(bill)
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            TextButton(onClick = { unpaidBills = emptyList() }, modifier = Modifier.fillMaxWidth()) {
                                Text("(Demo: Chuyển sang đã thanh toán hết)", fontSize = 12.sp, color = TextLightGray)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BgColor, RoundedCornerShape(12.dp))
                                .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(modifier = Modifier.size(56.dp).background(White, CircleShape).border(1.dp, GreenBorder, CircleShape), contentAlignment = Alignment.Center) {
                                Box(modifier = Modifier.size(56.dp).background(GreenText.copy(alpha = 0.1f), CircleShape))
                                Icon(Icons.Filled.CheckCircle, null, tint = GreenText, modifier = Modifier.size(28.dp))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Tuyệt vời!", fontWeight = FontWeight.SemiBold, color = TextDark)
                            Text("Không có hóa đơn nào chưa thanh toán", style = MaterialTheme.typography.bodySmall, color = TextGray)
                            TextButton(onClick = { unpaidBills = mockUnpaidBills }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                                Text("(Demo: Chuyển sang có hóa đơn)", fontSize = 12.sp, color = TextLightGray)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Block 2: History (Sử dụng component đã sửa lỗi timeline)
                PaymentCardSection(
                    title = "Lịch sử thanh toán",
                    icon = Icons.Filled.History,
                    iconColor = IndigoPrimary
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                        mockHistoryData.forEachIndexed { index, item ->
                            PaymentHistoryRow(
                                item = item,
                                isLast = index == mockHistoryData.lastIndex,
                                onClick = { selectedBill = item }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Block 3
                PaymentCardSection(title = "Hỗ trợ giao dịch và thanh toán", icon = Icons.Filled.AccountBalanceWallet, iconColor = AmberText) {
                    Text("Nếu bạn gặp bất kỳ vấn đề nào liên quan đến hóa đơn hoặc thanh toán, vui lòng liên hệ đội ngũ hỗ trợ.", style = MaterialTheme.typography.bodySmall, color = TextGray, modifier = Modifier.padding(bottom = 20.dp), lineHeight = 20.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        PaymentSupportBox(IndigoLight, IndigoBorder, Icons.Filled.Phone, IndigoPrimary, "Hotline", "1900-1000", IndigoPrimary)
                        PaymentSupportBox(AmberBg, AmberBorder, Icons.Filled.AccessTime, AmberText, "Giờ làm việc", "8:00 - 17:00", AmberText, "(T2-T6)")
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

// --- SCREEN DETAIL ---
@Composable
fun BillDetailScreen(bill: PaymentHistoryItem, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(BgColor)) {
        Surface(color = White, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = TextDark) }
                Spacer(modifier = Modifier.width(8.dp))
                Column { Text("Chi tiết hóa đơn", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark); Text("Xem thông tin chi tiết hóa đơn của bạn", style = MaterialTheme.typography.bodySmall, color = TextGray) }
            }
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = White), shape = RoundedCornerShape(16.dp), border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column { Text("SỐ HÓA ĐƠN", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold); Text(bill.id, fontWeight = FontWeight.Bold, color = TextDark) }
                        Surface(color = GreenBg, shape = CircleShape, modifier = Modifier.padding(start = 8.dp)) {
                            Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) { Icon(Icons.Filled.CheckCircle, null, tint = GreenText, modifier = Modifier.size(14.dp)); Spacer(modifier = Modifier.width(4.dp)); Text(bill.status, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenText) }
                        }
                    }
                    HorizontalDivider(color = BorderColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailRow(Icons.Filled.Description, "Tên hóa đơn", bill.billName)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(Icons.Filled.CalendarToday, "Hạn thanh toán", bill.dueDate)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailRow(Icons.Filled.AccessTime, "Ngày thanh toán", bill.paymentDate)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Card(colors = CardDefaults.cardColors(containerColor = White), shape = RoundedCornerShape(16.dp), border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor), modifier = Modifier.fillMaxWidth()) {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().background(BgColor).padding(horizontal = 20.dp, vertical = 12.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("TÊN DỊCH VỤ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextLightGray); Text("THÀNH TIỀN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextLightGray) }
                    }
                    Column(modifier = Modifier.padding(20.dp)) {
                        mockServiceDetailsFull.forEachIndexed { index, item ->
                            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Column { Text(item.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark); Text("${item.qty} x ${item.price}", fontSize = 12.sp, color = TextGray) }
                                Text(item.total, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                            }
                            if (index < mockServiceDetailsFull.lastIndex) HorizontalDivider(color = BorderColor, modifier = Modifier.padding(bottom = 12.dp))
                        }
                        HorizontalDivider(color = BorderColor, thickness = 1.dp, modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Tổng cộng", fontWeight = FontWeight.Medium, color = TextDark); Text(bill.amount, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = IndigoPrimary) }
                    }
                }
            }
        }
    }
}