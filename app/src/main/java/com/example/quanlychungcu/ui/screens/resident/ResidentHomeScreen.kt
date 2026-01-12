package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.ui.theme.*

@Composable
fun HomeScreen(
    unreadCount: Int,
    processingReportCount: Int,
    onNavigate: (ResidentScreen) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        // 1. Header Chào mừng
        item {
            Text("Chào mừng trở lại!", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Text("Hôm nay là Thứ Bảy, 10 tháng 1, 2026", fontSize = 14.sp, color = TextGray)
            Spacer(Modifier.height(24.dp))
        }

        // 2. Các thẻ thống kê (Click để navigate)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoCard("Hóa đơn chưa thanh toán", "1", RedError, RedError.copy(alpha = 0.1f), Icons.Outlined.Receipt) { onNavigate(ResidentScreen.PAYMENT) }
                InfoCard("Căn hộ đang sở hữu", "1", GreenSuccess, GreenSuccess.copy(alpha = 0.1f), Icons.Outlined.Home) {}
                InfoCard("Phản ánh đang xử lý", processingReportCount.toString(), OrangeWarning, OrangeWarning.copy(alpha = 0.1f), Icons.Outlined.WarningAmber) { onNavigate(ResidentScreen.REPORT) }
                InfoCard("Thông báo mới", unreadCount.toString(), BlueInfo, BlueInfo.copy(alpha = 0.1f), Icons.Outlined.Notifications) { onNavigate(ResidentScreen.NOTIFICATION) }
            }
            Spacer(Modifier.height(32.dp))
        }

        // 3. CHI TIẾT CĂN HỘ (ĐÃ SỬA GIỐNG MẪU 100%)
        item {
            Text("Chi tiết căn hộ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFEEEEEE))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Header Card: Tên + Badge Status
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Căn hộ A101", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Tòa A - Tầng 1 - Luxury Residence", fontSize = 13.sp, color = TextGray)
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            color = GreenBg,
                            shape = RoundedCornerShape(20.dp),
                            // border = BorderStroke(1.dp, GreenBorder) // Mẫu không có border, chỉ có nền
                        ) {
                            Text(
                                text = "Đang ở",
                                color = GreenText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Group 1: Thông số căn hộ
                    Text("Thông số căn hộ", fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))

                    ApartmentDetailBox(Icons.Outlined.Analytics, "Diện tích", "40 m²") // Icon biểu đồ
                    Spacer(modifier = Modifier.height(12.dp))
                    ApartmentDetailBox(Icons.Outlined.Article, "Loại căn hộ", "Studio") // Icon văn bản/loại

                    Spacer(modifier = Modifier.height(24.dp))

                    // Group 2: Thông tin chi tiết
                    Text("Thông tin chi tiết", fontSize = 14.sp, color = TextGray, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(12.dp))

                    ApartmentDetailBox(Icons.Outlined.CalendarToday, "Ngày bắt đầu sử dụng", "05/01/2026")
                    Spacer(modifier = Modifier.height(12.dp))
                    ApartmentDetailBox(Icons.Outlined.Videocam, "Mô tả căn hộ", "Căn hộ Studio, diện tích nhỏ gọn") // Icon Camera
                }
            }
            Spacer(Modifier.height(32.dp))
        }

        // 4. Danh sách dịch vụ
        item {
            Text("Danh sách dịch vụ", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(20.dp))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Row(Modifier.fillMaxWidth()) {
                        Text("Tên dịch vụ", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.3f))
                        Text("Giá tiền", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("Đơn vị", fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
                    }
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(Modifier.height(12.dp))

                    ServiceRow("Phí quản lý", "15.000 đ", "m²/tháng")
                    ServiceRow("Điện", "2.500 đ", "kWh")
                    ServiceRow("Nước", "15.000 đ", "m³")
                    ServiceRow("Internet", "200.000 đ", "tháng")
                    ServiceRow("Giữ xe ô tô", "1.200.000 đ", "tháng")
                    ServiceRow("Giữ xe máy", "100.000 đ", "tháng")
                    ServiceRow("Phòng Gym", "300.000 đ", "tháng")
                    ServiceRow("Hồ bơi", "500.000 đ", "tháng")
                }
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

// --- SUB COMPONENTS ---

@Composable
fun InfoCard(title: String, value: String, color: Color, bgColor: Color, icon: ImageVector, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(18.dp))
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column { Text(title, fontSize = 13.sp, color = TextGray); Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color) }
            Box(modifier = Modifier.size(44.dp).background(bgColor, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) { Icon(icon, null, tint = color, modifier = Modifier.size(24.dp)) }
        }
    }
}

// Component mới: Box chi tiết căn hộ nền xám
@Composable
fun ApartmentDetailBox(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp)) // Nền xám nhạt giống ảnh
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextDark, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(label, fontSize = 12.sp, color = TextGray)
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        }
    }
}

@Composable
fun ServiceRow(name: String, price: String, unit: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).background(Color.White, RoundedCornerShape(14.dp)).border(1.dp, Color(0xFFF5F5F5), RoundedCornerShape(14.dp)).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(name, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.3f))
        Text(price, fontSize = 14.sp, modifier = Modifier.weight(1f))
        Text(unit, fontSize = 14.sp, color = TextGray, modifier = Modifier.weight(0.7f), textAlign = TextAlign.End)
    }
}