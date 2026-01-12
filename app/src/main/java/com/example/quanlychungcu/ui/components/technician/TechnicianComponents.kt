package com.example.quanlychungcu.ui.components.technician

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.data.model.RequestItem // Đã đổi tên thành RequestItem theo yêu cầu refactor

// --- COLORS ---
val ColorBlue700 = Color(0xFF1976D2)
val ColorRed600 = Color(0xFFE53935)
val ColorGreen600 = Color(0xFF43A047)
val ColorGray50 = Color(0xFFF9FAFB)
val ColorGray100 = Color(0xFFF3F4F6)
val ColorGray200 = Color(0xFFE5E7EB)
val ColorGray500 = Color(0xFF6B7280)
val ColorGray900 = Color(0xFF111827)

data class StatusConfig(val text: String, val color: Color, val bg: Color, val border: Color)

@Composable
fun RequestCard(
    req: RequestItem,
    showHistory: Boolean,
    relativeTime: String,
    onQuickAction: () -> Unit,
    onDetailClick: () -> Unit,
    onCancel: () -> Unit
) {
    val (statusText, statusColor, statusBg, borderColor) = when (req.status) {
        "processing" -> StatusConfig("Đang xử lý", ColorBlue700, Color(0xFFEFF6FF), Color(0xFFDBEAFE))
        "accepted" -> StatusConfig("Chờ xử lý", ColorRed600, Color(0xFFFEF2F2), Color(0xFFFEE2E2))
        "completed" -> StatusConfig("Hoàn thành", ColorGreen600, Color(0xFFF0FDF4), Color(0xFFDCFCE7))
        "cancelled" -> StatusConfig("Đã hủy", ColorGray500, ColorGray100, ColorGray200)
        else -> StatusConfig("Chờ xử lý", ColorRed600, Color.White, ColorGray200)
    }

    val cardBg = if (req.status == "cancelled") Color(0xFFF9FAFB).copy(alpha = 0.75f) else when (req.status) {
        "processing" -> Color(0xFFEFF6FF)
        "accepted" -> Color(0xFFFEF2F2)
        "completed" -> Color(0xFFF0FDF4)
        else -> Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() }
            // --- SỬA LỖI TẠI ĐÂY: Chuyển border vào modifier ---
            .border(1.dp, borderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
        // Đã xóa tham số border = ... ở đây vì Card không hỗ trợ
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            // Badge Status
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(statusBg, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(statusText, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = statusColor)
            }

            Column {
                Text(
                    req.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = ColorGray900,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 80.dp, bottom = 6.dp)
                )

                Row(modifier = Modifier.padding(bottom = 12.dp)) {
                    Text("Báo cáo bởi: ", fontSize = 12.sp, color = ColorGray500)
                    Text(req.reporter, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF374151))
                }

                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Schedule, null, modifier = Modifier.size(14.dp), tint = Color(0xFF9CA3AF))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(relativeTime, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF9CA3AF))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (!showHistory && req.status != "cancelled") {
                        Button(
                            onClick = onQuickAction,
                            modifier = Modifier.weight(1f).height(40.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (req.status == "processing") ColorBlue700 else ColorRed600
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(
                                if (req.status == "processing") "Báo cáo" else "Xử lý ngay",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (req.status == "processing") {
                            Button(
                                onClick = onCancel,
                                modifier = Modifier.weight(1f).height(40.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, ColorRed600),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Hủy", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ColorRed600)
                            }
                        }
                    }

                    Button(
                        onClick = onDetailClick,
                        modifier = Modifier.weight(1f).height(40.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, ColorGray200),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Chi tiết", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(16.dp), tint = Color(0xFF9CA3AF))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, fontSize = 14.sp, color = ColorGray500, modifier = Modifier.width(100.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = ColorGray900)
        }
        Divider(color = ColorGray100, modifier = Modifier.fillMaxWidth())
    }
}