package com.example.quanlychungcu.ui.screens.technician

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

import com.example.quanlychungcu.ui.theme.*
import com.example.quanlychungcu.data.model.RequestItem
import com.example.quanlychungcu.data.model.CompletionReport
import com.example.quanlychungcu.ui.components.technician.*

// --- ENUM MÀN HÌNH ---
enum class TechScreen {
    DASHBOARD, // Màn hình chính (Công việc)
    PROFILE    // Hồ sơ cá nhân
}

// --- MENU DATA ---
data class TechMenuItem(
    val icon: ImageVector,
    val label: String,
    val screen: TechScreen
)

data class TechMenuGroup(
    val title: String,
    val items: List<TechMenuItem>
)

//@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechnicianDashboardScreen(onLogout: () -> Unit) {
    // --- STATE ---
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentScreen by remember { mutableStateOf(TechScreen.DASHBOARD) }
    var isUserMenuExpanded by remember { mutableStateOf(false) }

    // --- LOGIC STATE ---
    var showHistory by remember { mutableStateOf(false) }
    var selectedRequest by remember { mutableStateOf<RequestItem?>(null) }
    var isCompleting by remember { mutableStateOf(false) }
    var completionReport by remember { mutableStateOf(CompletionReport()) }

    var requests by remember {
        mutableStateOf(
            listOf(
                RequestItem(
                    "REQ-001",
                    "Sửa bóng đèn hành lang",
                    "Nguyễn Văn A",
                    "A502 - Tòa A",
                    "Điện",
                    "Bóng đèn nhấp nháy liên tục.",
                    "high",
                    "accepted",
                    "09:30 - 10/01/2026",
                    "me",
                    images = listOf("https://images.unsplash.com/photo-1544724569-5f546fd6dd2d")
                ),
                RequestItem(
                    "REQ-003",
                    "Kẹt cửa tự động sảnh A",
                    "Lễ tân - Trần Thị B",
                    "Sảnh chính",
                    "Thiết bị công cộng",
                    "Cửa tự động sảnh A bị kẹt.",
                    "high",
                    "processing",
                    "16:00 - 09/01/2026",
                    "me"
                ),
                RequestItem(
                    "REQ-004",
                    "Thay vòi nước bồn rửa",
                    "Lê Văn C",
                    "B0505 - Tòa B",
                    "Nước",
                    "Vòi nước bồn rửa tay bị rò rỉ.",
                    "medium",
                    "accepted",
                    "08:00 - 10/01/2026",
                    "me"
                ),
                RequestItem(
                    "REQ-005",
                    "Điều hòa báo lỗi E4",
                    "Phạm Thị D",
                    "C1202 - Tòa C",
                    "Điều hòa",
                    "Điều hòa không mát, báo lỗi E4.",
                    "medium",
                    "completed",
                    "14:00 - 08/01/2026",
                    "me",
                    completedAt = "16:30 - 08/01/2026"
                )
            )
        )
    }

    // --- MENU CONFIG ---
    val menuGroups = listOf(
        TechMenuGroup(
            "QUẢN LÝ", listOf(
                TechMenuItem(Icons.Default.Assignment, "Công việc", TechScreen.DASHBOARD)
            )
        ),
        TechMenuGroup(
            "THÔNG TIN CÁ NHÂN", listOf(
                TechMenuItem(Icons.Default.Person, "Hồ sơ cá nhân", TechScreen.PROFILE)
            )
        )
    )

    // --- HELPERS ---
    fun updateRequestState(updatedItem: RequestItem) {
        requests = requests.map { if (it.id == updatedItem.id) updatedItem else it }
    }

    fun getRelativeTime(dateStr: String): String {
        return try {
            val parts = dateStr.split(" - ")
            val timePart = parts[0].split(":")
            val datePart = parts[1].split("/")
            val itemDate = LocalDateTime.of(
                datePart[2].toInt(),
                datePart[1].toInt(),
                datePart[0].toInt(),
                timePart[0].toInt(),
                timePart[1].toInt()
            )
            val now = LocalDateTime.of(2026, 1, 11, 12, 0)
            val diffSeconds = ChronoUnit.SECONDS.between(itemDate, now)
            when {
                diffSeconds < 60 -> "Vừa xong"
                diffSeconds < 3600 -> "${diffSeconds / 60} phút trước"
                diffSeconds < 86400 -> "${diffSeconds / 3600} giờ trước"
                diffSeconds < 2592000 -> "${diffSeconds / 86400} ngày trước"
                else -> dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }

    fun handleQuickAction(req: RequestItem) {
        if (req.status == "accepted") updateRequestState(req.copy(status = "processing"))
        else if (req.status == "processing") {
            selectedRequest = req; isCompleting = true
        }
    }

    fun handleSubmitCompletion() {
        selectedRequest?.let { req ->
            val nowStr =
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm - dd/MM/yyyy"))
            updateRequestState(req.copy(status = "completed", completedAt = nowStr))
            isCompleting = false; selectedRequest = null; completionReport = CompletionReport()
        }
    }

    val displayRequests = requests.filter { req ->
        if (showHistory) req.technician == "me" && req.status == "completed" else req.technician == "me" && req.status != "completed"
    }

    // --- UI ---
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(280.dp)
            ) {
                Spacer(Modifier.height(24.dp))
                menuGroups.forEach { group ->
                    Text(
                        text = group.title,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorGray500
                    )
                    group.items.forEach { item ->
                        val isActive = currentScreen == item.screen
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isActive) ColorBlue700.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable {
                                    currentScreen = item.screen
                                    scope.launch { drawerState.close() }
                                }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (isActive) ColorBlue700 else ColorGray500,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = item.label,
                                color = if (isActive) ColorBlue700 else ColorGray500,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    ) {
        Scaffold(
            containerColor = BgColor,
            topBar = {
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
                                    Icons.Default.Apartment,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(
                                    "Luxury Residence",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                Text("Trang Kỹ Thuật", fontSize = 12.sp, color = TextGray)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, "Menu", tint = TextGray)
                        }
                    },
                    actions = {
                        Box {
                            IconButton(onClick = { isUserMenuExpanded = true }) {
                                Icon(Icons.Outlined.Person, "User", tint = TextDark)
                            }
                            DropdownMenu(
                                expanded = isUserMenuExpanded,
                                onDismissRequest = { isUserMenuExpanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .width(240.dp)
                            ) {
                                // --- PHẦN HEADER HIỂN THỊ TÊN & VỊ TRÍ ---
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 12.dp
                                    )
                                ) {
                                    Text(
                                        text = "Trần Văn B", // Tên kỹ thuật viên
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Kỹ thuật viên - NV-0824", // Vị trí - Mã NV
                                        fontSize = 13.sp,
                                        color = TextGray
                                    )
                                }
                                HorizontalDivider(color = BorderColor) // Đường kẻ phân cách
                                // ------------------------------------------

                                DropdownMenuItem(
                                    text = { Text("Hồ sơ cá nhân", color = TextDark) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.Outlined.Person,
                                            null,
                                            modifier = Modifier.size(20.dp),
                                            tint = TextDark
                                        )
                                    },
                                    onClick = {
                                        isUserMenuExpanded = false;
                                        currentScreen = TechScreen.PROFILE
                                    }
                                )
                                HorizontalDivider(color = BorderColor)
                                DropdownMenuItem(
                                    text = { Text("Đăng xuất", color = RedError) },
                                    leadingIcon = {
                                        Icon(
                                            Icons.AutoMirrored.Outlined.Logout,
                                            null,
                                            tint = RedError,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    onClick = { isUserMenuExpanded = false; onLogout() }
                                )
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()) {
                when (currentScreen) {
                    TechScreen.DASHBOARD -> DashboardContent(
                        showHistory = showHistory,
                        onToggleHistory = { showHistory = !showHistory },
                        requests = displayRequests,
                        onRelativeTime = { getRelativeTime(it) },
                        onQuickAction = { req -> handleQuickAction(req) },
                        onDetailClick = { req -> selectedRequest = req; isCompleting = false },
                        onCancel = { req -> updateRequestState(req.copy(status = "accepted")) }
                    )

                    TechScreen.PROFILE -> TechnicianProfileScreen()
                }
            }
        }

        // --- MODAL DIALOG ---
        if (selectedRequest != null) {
            Dialog(
                onDismissRequest = { selectedRequest = null },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.6f))
                        .clickable(enabled = false) {}, contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight(0.9f)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ColorGray50)
                                    .padding(horizontal = 20.dp, vertical = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    if (isCompleting) "Báo cáo kết quả" else "Chi tiết báo cáo",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ColorGray900
                                )
                                IconButton(
                                    onClick = { selectedRequest = null },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color.White, CircleShape)
                                        .border(1.dp, ColorGray200, CircleShape)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            HorizontalDivider(color = ColorGray100)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .verticalScroll(rememberScrollState())
                                    .padding(20.dp)
                            ) {
                                if (!isCompleting) {
                                    Text(
                                        selectedRequest!!.title,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorBlue700,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                    DetailRow(
                                        Icons.Default.AccountCircle,
                                        "Người báo cáo:",
                                        selectedRequest!!.reporter
                                    )
                                    DetailRow(
                                        Icons.Default.Schedule,
                                        "Thời gian:",
                                        selectedRequest!!.time
                                    )
                                    Spacer(Modifier.height(20.dp))
                                    Text(
                                        "MÔ TẢ",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorGray500
                                    ); Spacer(Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(ColorGray50, RoundedCornerShape(12.dp))
                                            .border(1.dp, ColorGray100, RoundedCornerShape(12.dp))
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            selectedRequest!!.description,
                                            fontSize = 14.sp,
                                            color = ColorGray900,
                                            lineHeight = 20.sp
                                        )
                                    }
                                    Spacer(Modifier.height(20.dp))
                                    Text(
                                        "HÌNH ẢNH",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorGray500
                                    ); Spacer(Modifier.height(8.dp))
                                    if (selectedRequest!!.images.isNotEmpty()) {
                                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                            selectedRequest!!.images.forEach { url ->
                                                AsyncImage(
                                                    model = url,
                                                    contentDescription = null,
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .size(80.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(ColorGray200)
                                                )
                                            }
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(ColorGray50, RoundedCornerShape(12.dp))
                                                .border(
                                                    1.dp,
                                                    ColorGray200,
                                                    RoundedCornerShape(12.dp)
                                                )
                                                .padding(24.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Image,
                                                    null,
                                                    tint = ColorGray500,
                                                    modifier = Modifier.size(18.dp)
                                                ); Spacer(Modifier.width(8.dp)); Text(
                                                "Không có hình ảnh",
                                                fontSize = 12.sp,
                                                color = ColorGray500
                                            )
                                            }
                                        }
                                    }
                                } else {
                                    Text(
                                        "Công việc đã thực hiện",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorGray900
                                    ); Spacer(Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = completionReport.description,
                                        onValueChange = {
                                            completionReport =
                                                completionReport.copy(description = it)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        placeholder = { Text("Mô tả chi tiết...") },
                                        minLines = 3,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ColorBlue700,
                                            unfocusedBorderColor = ColorGray200
                                        )
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    Text(
                                        "Nguyên nhân sự cố",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ColorGray900
                                    ); Spacer(Modifier.height(6.dp))
                                    OutlinedTextField(
                                        value = completionReport.cause,
                                        onValueChange = {
                                            completionReport = completionReport.copy(cause = it)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(12.dp),
                                        placeholder = { Text("Ví dụ: Chập mạch...") },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = ColorBlue700,
                                            unfocusedBorderColor = ColorGray200
                                        )
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = { },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ColorBlue700.copy(0.1f)
                                        ),
                                        border = BorderStroke(1.dp, ColorBlue700.copy(0.3f))
                                    ) {
                                        Icon(
                                            Icons.Default.CameraAlt,
                                            null,
                                            tint = ColorBlue700,
                                            modifier = Modifier.size(18.dp)
                                        ); Spacer(Modifier.width(8.dp)); Text(
                                        "Chụp ảnh kết quả",
                                        color = ColorBlue700,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    }
                                    Spacer(Modifier.height(24.dp)); HorizontalDivider(color = ColorGray50); Spacer(
                                        Modifier.height(16.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Button(
                                            onClick = { isCompleting = false },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                            border = BorderStroke(1.dp, ColorGray200)
                                        ) {
                                            Text(
                                                "Quay lại",
                                                color = ColorGray900,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Button(
                                            onClick = { handleSubmitCompletion() },
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = ColorGreen600)
                                        ) {
                                            Text(
                                                "Xác nhận xong",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                            if (!isCompleting) {
                                HorizontalDivider(color = ColorGray100)
                                Box(modifier = Modifier.padding(20.dp)) {
                                    Button(
                                        onClick = {
                                            selectedRequest = null
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                        border = BorderStroke(1.dp, ColorGray200)
                                    ) {
                                        Text(
                                            "Đóng",
                                            color = ColorGray900,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- DASHBOARD CONTENT ---
@Composable
fun DashboardContent(
    showHistory: Boolean,
    onToggleHistory: () -> Unit,
    requests: List<RequestItem>,
    onRelativeTime: (String) -> String,
    onQuickAction: (RequestItem) -> Unit,
    onDetailClick: (RequestItem) -> Unit,
    onCancel: (RequestItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)) {
                Text(
                    "Bảo trì và phản ánh",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorGray900
                )
                Text(
                    "Theo dõi bảo trì thiết bị và phản ánh sự cố",
                    fontSize = 14.sp,
                    color = ColorGray500,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, ColorGray100),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            if (showHistory) "Lịch sử hoạt động" else "Báo cáo sự cố",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = ColorGray900
                        )
                        OutlinedButton(
                            onClick = onToggleHistory,
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, ColorGray200)
                        ) {
                            Icon(
                                if (showHistory) Icons.Default.Close else Icons.Default.History,
                                null,
                                modifier = Modifier.size(14.dp),
                                tint = ColorGray900
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (showHistory) "Đóng" else "Lịch sử",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorGray900
                            )
                        }
                    }
                    if (requests.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.Assignment,
                                null,
                                modifier = Modifier.size(48.dp),
                                tint = ColorGray200
                            )
                            Spacer(Modifier.height(12.dp)); Text(
                            if (showHistory) "Chưa có lịch sử" else "Không có sự cố nào",
                            color = ColorGray500,
                            fontSize = 14.sp
                        )
                        }
                    } else {
                        requests.forEach { req ->
                            RequestCard(
                                req = req,
                                showHistory = showHistory,
                                relativeTime = onRelativeTime(req.time),
                                onQuickAction = { onQuickAction(req) },
                                onDetailClick = { onDetailClick(req) },
                                onCancel = { onCancel(req) }
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}