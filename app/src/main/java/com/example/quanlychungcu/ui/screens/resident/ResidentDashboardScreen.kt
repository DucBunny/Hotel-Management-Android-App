package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

import com.example.quanlychungcu.data.model.ReportItem
import com.example.quanlychungcu.data.model.NotificationItem
import com.example.quanlychungcu.ui.theme.*

// Enum màn hình Cư dân
enum class ResidentScreen {
    HOME, PAYMENT, NOTIFICATION, PROFILE, REPORT
}

data class MenuItem(val icon: ImageVector, val label: String, val screen: ResidentScreen)
data class MenuGroup(val title: String, val items: List<MenuItem>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidentDashboardScreen(onLogout: () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(ResidentScreen.HOME) }

    // State cho Dropdown menu ở TopBar
    var isUserMenuExpanded by remember { mutableStateOf(false) }

    // --- DỮ LIỆU MẪU: PHẢN ÁNH ---
    val reports = remember { mutableStateListOf(
        ReportItem(
            id = "#PA-20260109-01",
            title = "Hỏng đèn hành lang tầng 5",
            content = "Đèn hành lang khu vực trước cửa thang máy A2 bị nhấp nháy liên tục.",
            status = "pending",
            date = "09/01/2026",
            time = "14:30",
            type = "Sửa chữa điện"
        ),
        ReportItem(
            id = "#PA-20260108-02",
            title = "Nước yếu khu vực bếp",
            content = "Vòi nước bồn rửa bát chảy rất yếu từ sáng nay.",
            status = "pending",
            date = "08/01/2026",
            time = "09:15",
            type = "Sửa ống nước"
        )
    )}
    val processingReportCount = reports.count { it.status == "pending" }

    fun handleAddReport(title: String, content: String, type: String, image: ImageBitmap?) {
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val newReport = ReportItem(
            id = "#PA-${System.currentTimeMillis()}",
            title = title,
            content = content,
            status = "pending",
            date = dateFormat.format(now.time),
            time = timeFormat.format(now.time),
            type = type,
            capturedImage = image
        )
        reports.add(0, newReport)
    }

    // --- DỮ LIỆU MẪU: THÔNG BÁO ---
    val notifications = remember { mutableStateListOf(
        NotificationItem(5, "Khảo sát ý kiến cư dân", "Khác", "gray", "Ban quản lý mong nhận được ý kiến đóng góp...", "01:03", "06/01", false),
        NotificationItem(1, "Cập nhật hệ thống", "Hệ thống", "blue", "Nâng cấp hệ thống máy chủ vào lúc 0h ngày mai.", "09:30", "Hôm nay", false),
        NotificationItem(4, "Phí dịch vụ tháng 11", "Hóa đơn", "green", "Vui lòng thanh toán phí dịch vụ tháng 11.", "01:03", "06/01", true),
        NotificationItem(2, "Lịch cắt nước định kỳ", "Bảo trì", "orange", "Tạm ngưng cấp nước từ 14h-16h chiều nay.", "08:00", "Hôm qua", true)
    )}
    val unreadCount = notifications.count { !it.isRead }

    // --- MENU SIDEBAR ---
    val menuGroups = listOf(
        MenuGroup("TỔNG QUAN", listOf(MenuItem(Icons.Outlined.Home, "Trang chủ", ResidentScreen.HOME))),
        MenuGroup("DỊCH VỤ", listOf(
            MenuItem(Icons.Outlined.Receipt, "Thanh toán", ResidentScreen.PAYMENT),
            MenuItem(Icons.Outlined.Notifications, "Thông báo", ResidentScreen.NOTIFICATION),
            MenuItem(Icons.Outlined.ReportProblem, "Phản ánh", ResidentScreen.REPORT)
        )),
        MenuGroup("THÔNG TIN CÁ NHÂN", listOf(MenuItem(Icons.Outlined.Person, "Hồ sơ cá nhân", ResidentScreen.PROFILE)))
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = Color.White, modifier = Modifier.width(280.dp)) {
                Spacer(Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.padding(horizontal = 12.dp)) {
                    items(menuGroups.size) { index ->
                        val group = menuGroups[index]
                        Text(
                            text = group.title,
                            fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextGray,
                            modifier = Modifier.padding(start = 12.dp, top = 24.dp, bottom = 8.dp)
                        )
                        group.items.forEach { item ->
                            val isSelected = currentScreen == item.screen
                            NavigationDrawerItem(
                                label = { Text(item.label, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium) },
                                icon = { Icon(item.icon, null, tint = if (isSelected) BluePrimary else TextDark) },
                                badge = {
                                    if (item.screen == ResidentScreen.NOTIFICATION && unreadCount > 0) {
                                        Badge(containerColor = RedError) { Text(unreadCount.toString(), color = Color.White) }
                                    }
                                },
                                selected = isSelected,
                                onClick = { currentScreen = item.screen; scope.launch { drawerState.close() } },
                                colors = NavigationDrawerItemDefaults.colors(
                                    selectedContainerColor = LightBlueBg,
                                    selectedTextColor = BluePrimary,
                                    unselectedTextColor = TextDark,
                                    unselectedIconColor = TextDark
                                ),
                                modifier = Modifier.padding(vertical = 2.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            containerColor = BgColor,
            topBar = {
                // --- TOP BAR ĐƯỢC VIẾT TRỰC TIẾP TẠI ĐÂY ---
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(BluePrimary, RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Apartment,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Luxury Residence",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text(
                                    text = "Trang cư dân",
                                    fontSize = 12.sp,
                                    color = TextGray
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextGray)
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { isUserMenuExpanded = true }) {
                                Icon(Icons.Outlined.Person, contentDescription = "User", tint = TextDark)
                            }
                            DropdownMenu(
                                expanded = isUserMenuExpanded,
                                onDismissRequest = { isUserMenuExpanded = false },
                                modifier = Modifier.background(Color.White).width(240.dp)
                            ) {
                                // --- PHẦN HEADER HIỂN THỊ TÊN & VỊ TRÍ ---
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                    Text(
                                        text = "Nguyễn Văn A", // Tên cư dân
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Cư dân - Căn hộ A101", // Vai trò - Căn hộ
                                        fontSize = 13.sp,
                                        color = TextGray
                                    )
                                }
                                HorizontalDivider(color = BorderColor)
                                // ------------------------------------------
                                DropdownMenuItem(
                                    text = { Text("Hồ sơ cá nhân", color = TextDark) },
                                    leadingIcon = { Icon(Icons.Outlined.Person, null, modifier = Modifier.size(20.dp), tint = TextDark) },
                                    onClick = {
                                        isUserMenuExpanded = false
                                        currentScreen = ResidentScreen.PROFILE
                                    }
                                )
                                HorizontalDivider(color = BorderColor)
                                DropdownMenuItem(
                                    text = { Text("Đăng xuất", color = RedError) },
                                    leadingIcon = { Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = RedError, modifier = Modifier.size(20.dp)) },
                                    onClick = {
                                        isUserMenuExpanded = false
                                        onLogout()
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                when (currentScreen) {
                    ResidentScreen.HOME -> HomeScreen(
                        unreadCount = unreadCount,
                        processingReportCount = processingReportCount,
                        onNavigate = { screen -> currentScreen = screen }
                    )
                    ResidentScreen.PAYMENT -> PaymentScreen()
                    ResidentScreen.NOTIFICATION -> NotificationScreen(notifications)
                    ResidentScreen.REPORT -> ReportScreen(
                        reports = reports,
                        onAddReport = { t, c, tp, img -> handleAddReport(t, c, tp, img) }
                    )
                    ResidentScreen.PROFILE -> ResidentProfileScreen()
                }
            }
        }
    }
}