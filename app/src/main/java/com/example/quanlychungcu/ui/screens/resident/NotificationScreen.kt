package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.quanlychungcu.data.model.NotificationItem
import com.example.quanlychungcu.ui.theme.*

data class FilterOption(val label: String, val icon: ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notifications: SnapshotStateList<NotificationItem>
) {
    var selectedNotificationId by remember { mutableStateOf<Int?>(null) }
    var selectedFilter by remember { mutableStateOf("Tất cả") }

    val filters = listOf(
        FilterOption("Tất cả", Icons.Default.Notifications),
        FilterOption("Hóa đơn", Icons.Default.AttachMoney),
        FilterOption("Hệ thống", Icons.Default.Settings),
        FilterOption("Sự kiện", Icons.Default.CalendarToday),
        FilterOption("Bảo trì", Icons.Default.Build),
        FilterOption("Khác", Icons.Default.MoreHoriz)
    )

    val filteredNotifications = if (selectedFilter == "Tất cả") {
        notifications
    } else {
        notifications.filter { it.category == selectedFilter }
    }

    fun onCardClick(id: Int) { selectedNotificationId = id }

    fun onToggleReadStatus() {
        val currentId = selectedNotificationId ?: return
        val index = notifications.indexOfFirst { it.id == currentId }
        if (index != -1) {
            val newItem = notifications[index].copy(isRead = !notifications[index].isRead)
            notifications[index] = newItem
        }
    }

    fun onDismissPopup() { selectedNotificationId = null }

    Column(modifier = Modifier.fillMaxSize().background(BgColor).padding(16.dp)) {
        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 4.dp)) {
            Text(text = "Thông Báo Chung Cư", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
            Text(text = "Chào mừng cư dân! Cập nhật thông tin mới nhất", style = MaterialTheme.typography.bodyMedium, color = TextGray, fontWeight = FontWeight.Medium)
        }

        LazyRow(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = filter.label == selectedFilter
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedFilter = filter.label },
                    label = { Text(filter.label, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal) },
                    leadingIcon = { Icon(imageVector = filter.icon, contentDescription = null, modifier = Modifier.size(16.dp), tint = if (isSelected) BluePrimary else TextGray) },
                    colors = FilterChipDefaults.filterChipColors(containerColor = Color.White, selectedContainerColor = Color(0xFFEFF6FF), labelColor = TextGray, selectedLabelColor = BluePrimary, iconColor = TextGray, selectedLeadingIconColor = BluePrimary),
                    border = FilterChipDefaults.filterChipBorder(enabled = true, selected = isSelected, borderColor = Color(0xFFE5E7EB), selectedBorderColor = BluePrimary, borderWidth = 1.dp),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        if (filteredNotifications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.NotificationsOff, null, tint = TextGray, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Không có thông báo nào", color = TextGray)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxHeight()) {
                items(filteredNotifications) { notification ->
                    NotificationCard(notification = notification, onClick = { onCardClick(notification.id) })
                }
            }
        }
    }

    if (selectedNotificationId != null) {
        val selectedItem = notifications.find { it.id == selectedNotificationId }
        if (selectedItem != null) {
            NotificationPopupDialog(notification = selectedItem, onDismiss = { onDismissPopup() }, onToggleRead = { onToggleReadStatus() })
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem, onClick: () -> Unit) {
    val tagColors = getTagColors(notification.color)
    val isRead = notification.isRead
    val bgColor = if (isRead) Color.White else Color(0xFFF0F9FF)
    val borderColor = if (isRead) Color.Transparent else Color(0xFFDBEAFE)

    Card(onClick = onClick, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = bgColor), elevation = CardDefaults.cardElevation(defaultElevation = if (isRead) 0.dp else 2.dp), modifier = Modifier.fillMaxWidth().border(width = if (isRead) 0.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.Top) {
                    if (!isRead) { Box(modifier = Modifier.padding(top = 6.dp, end = 8.dp).size(8.dp).background(BluePrimary, CircleShape)) }
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
                            Text(text = notification.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = if (!isRead) BluePrimary else TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                            Spacer(modifier = Modifier.width(8.dp))
                            TagBadge(text = notification.category, colors = tagColors)
                        }
                    }
                }
                Text(text = notification.time, style = MaterialTheme.typography.labelSmall, color = if (!isRead) BluePrimary else TextGray, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(start = 8.dp, top = 2.dp))
            }
            Text(text = notification.content, style = MaterialTheme.typography.bodySmall, color = TextDark, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp, modifier = Modifier.padding(start = if (!isRead) 16.dp else 0.dp, bottom = 12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) { Box(modifier = Modifier.weight(1f).height(1.dp).background(if (isRead) Color(0xFFF3F4F6) else Color(0xFFBFDBFE))) }
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.End) { Text(text = notification.date, style = MaterialTheme.typography.labelSmall, color = if (isRead) Color(0xFF9CA3AF) else Color(0xFF60A5FA)) }
        }
    }
}

@Composable
fun NotificationPopupDialog(notification: NotificationItem, onDismiss: () -> Unit, onToggleRead: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(8.dp), modifier = Modifier.fillMaxWidth()) {
            Column {
                Row(modifier = Modifier.fillMaxWidth().background(Color(0xFFF9FAFB)).padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Chi tiết thông báo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp).background(Color(0xFFE5E7EB), CircleShape)) { Icon(Icons.Default.Close, "Close", tint = Color(0xFF4B5563), modifier = Modifier.size(16.dp)) }
                }
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(text = notification.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = BluePrimary, modifier = Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(8.dp))
                        TagBadge(text = notification.category, colors = getTagColors(notification.color))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
                        Icon(Icons.Default.Schedule, null, tint = TextGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(notification.time, style = MaterialTheme.typography.labelSmall, color = TextGray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(modifier = Modifier.size(4.dp).background(Color.LightGray, CircleShape))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(notification.date, style = MaterialTheme.typography.labelSmall, color = TextGray)
                    }
                    HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(bottom = 20.dp))
                    Text(text = notification.content, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1F2937), lineHeight = 22.sp, modifier = Modifier.padding(bottom = 24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF111827), contentColor = Color.White), shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(0.dp), modifier = Modifier.weight(0.35f).height(48.dp)) { Text("Đóng", fontWeight = FontWeight.Bold) }
                        val isRead = notification.isRead
                        val btnBgColor = if (isRead) Color.White else GreenSuccess
                        val btnTextColor = if (isRead) TextDark else Color.White
                        val btnBorder = if (isRead) BorderStroke(1.dp, Color(0xFFE5E7EB)) else null
                        val buttonText = if (isRead) "Đánh dấu chưa đọc" else "Đánh dấu đã đọc"
                        Button(onClick = onToggleRead, colors = ButtonDefaults.buttonColors(containerColor = btnBgColor, contentColor = btnTextColor), border = btnBorder, shape = RoundedCornerShape(12.dp), contentPadding = PaddingValues(horizontal = 4.dp), modifier = Modifier.weight(0.65f).height(48.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!isRead) { Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(6.dp)) }
                                Text(text = buttonText, fontSize = 12.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TagColors(val bg: Color, val text: Color, val border: Color)
fun getTagColors(color: String): TagColors {
    return when (color) {
        "blue" -> TagColors(Color(0xFFDBEAFE), Color(0xFF1D4ED8), Color(0xFFBFDBFE))
        "orange" -> TagColors(Color(0xFFFFEDD5), Color(0xFFC2410C), Color(0xFFFED7AA))
        "purple" -> TagColors(Color(0xFFF3E8FF), Color(0xFF7E22CE), Color(0xFFE9D5FF))
        "red" -> TagColors(Color(0xFFFEE2E2), Color(0xFFB91C1C), Color(0xFFFECACA))
        "green" -> TagColors(Color(0xFFDCFCE7), Color(0xFF166534), Color(0xFFBBF7D0))
        "gray" -> TagColors(Color(0xFFF3F4F6), Color(0xFF4B5563), Color(0xFFE5E7EB))
        else -> TagColors(Color(0xFFF3F4F6), Color(0xFF4B5563), Color(0xFFE5E7EB))
    }
}
@Composable
fun TagBadge(text: String, colors: TagColors) {
    Surface(color = colors.bg, shape = RoundedCornerShape(4.dp), border = BorderStroke(1.dp, colors.border)) {
        Text(text = text, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = colors.text)
    }
}