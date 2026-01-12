package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

import com.example.quanlychungcu.ui.theme.*
// Import các component dùng chung
import com.example.quanlychungcu.ui.components.profile.FieldConfig
import com.example.quanlychungcu.ui.components.profile.ProfileDetailItem
import com.example.quanlychungcu.ui.components.profile.ChangePasswordDialog

// MODEL CỤC BỘ CHO CƯ DÂN
data class ResidentProfile(
    var name: String,
    val role: String,
    val apartment: String,
    val joinDate: String,
    // Thông tin chi tiết
    var fullName: String,
    var email: String,
    var phone: String,
    var gender: String,
    var idCard: String,
    var dob: String,
    var hometown: String,
    var ethnicity: String,
    var job: String,
    var householdBook: String
)

@Composable
fun ResidentProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showSuccessToast by remember { mutableStateOf(false) }

    // DỮ LIỆU MẪU CƯ DÂN
    var profile by remember { mutableStateOf(
        ResidentProfile(
            name = "Nguyễn Văn A",
            role = "Chủ hộ",
            apartment = "A101",
            joinDate = "01/01/2023",
            fullName = "Nguyễn Văn A",
            email = "nguyenvana@gmail.com",
            phone = "0901 888 888",
            gender = "Nam",
            idCard = "001200001234",
            dob = "15/08/1990",
            hometown = "Hà Nội",
            ethnicity = "Kinh",
            job = "Kỹ sư",
            householdBook = "HH1"
        )
    )}
    var editForm by remember { mutableStateOf(profile.copy()) }

    // CẤU HÌNH FIELD CHO CƯ DÂN (Đầy đủ nội dung cũ)
    val fieldConfigs = mapOf(
        "fullName" to FieldConfig("Họ và tên", Icons.Default.Person, Color(0xFF2563EB), Color(0xFFEFF6FF)),
        "email" to FieldConfig("Email", Icons.Default.Email, Color(0xFFEA580C), Color(0xFFFFF7ED)),
        "phone" to FieldConfig("Số điện thoại", Icons.Default.Phone, Color(0xFF16A34A), Color(0xFFF0FDF4)),
        "gender" to FieldConfig("Giới tính", Icons.Default.Face, Color(0xFF9333EA), Color(0xFFFAF5FF), "select", listOf("Nam", "Nữ", "Khác")),
        "idCard" to FieldConfig("CCCD/CMND", Icons.Default.CreditCard, Color(0xFF4F46E5), Color(0xFFEEF2FF)),
        "dob" to FieldConfig("Ngày sinh", Icons.Default.CalendarToday, Color(0xFFDB2777), Color(0xFFFDF2F8), "date"),
        "hometown" to FieldConfig("Quê quán", Icons.Default.Place, Color(0xFFDC2626), Color(0xFFFEF2F2)),
        "ethnicity" to FieldConfig("Dân tộc", Icons.Default.Group, Color(0xFF0D9488), Color(0xFFF0FDFA)),
        "job" to FieldConfig("Nghề nghiệp", Icons.Default.Work, Color(0xFF0891B2), Color(0xFFECFEFF)),
        "householdBook" to FieldConfig("Sổ hộ khẩu", Icons.Default.Description, Color(0xFFCA8A04), Color(0xFFFEFCE8))
    )

    fun startEditing() { editForm = profile.copy(); isEditing = true }
    fun cancelEditing() { isEditing = false }
    fun saveChanges() { profile = editForm.copy(); isEditing = false }

    LaunchedEffect(showSuccessToast) { if (showSuccessToast) { delay(3000); showSuccessToast = false } }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        // Toast thông báo
        AnimatedVisibility(
            visible = showSuccessToast,
            enter = slideInVertically { -it } + fadeIn(),
            exit = slideOutVertically { -it } + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp).zIndex(100f)
        ) {
            Surface(color = GreenBg, border = BorderStroke(1.dp, GreenBorder), shape = RoundedCornerShape(12.dp), shadowElevation = 4.dp) {
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(Icons.Default.CheckCircle, null, tint = GreenSuccess)
                    Spacer(Modifier.width(8.dp))
                    Text("Cập nhật thành công!", color = GreenText, fontWeight = FontWeight.Bold)
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Header
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp, top = 4.dp)) {
                Text(text = if (isEditing) "Chỉnh sửa hồ sơ" else "Hồ sơ cá nhân", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
                Text(text = "Quản lý thông tin cá nhân và gia đình", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }

            // Card Profile (Đã đồng bộ layout với Technician)
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF3F4F6)), modifier = Modifier.fillMaxWidth()) {
                Column {
                    // --- PHẦN 1: AVATAR & INFO (GIỐNG KỸ THUẬT VIÊN) ---
                    Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.padding(bottom = 16.dp)) {
                            Box(modifier = Modifier.size(96.dp).background(Color(0xFFEFF6FF), CircleShape).border(4.dp, Color.White, CircleShape).shadow(4.dp, CircleShape), contentAlignment = Alignment.Center) {
                                // Icon Cư dân (Person)
                                Icon(Icons.Default.Person, null, tint = BluePrimary, modifier = Modifier.size(48.dp))
                            }
                            if (isEditing) {
                                Surface(onClick = { /* Change Photo */ }, color = BluePrimary, shape = RoundedCornerShape(50), modifier = Modifier.align(Alignment.BottomEnd).offset(x = 8.dp), border = BorderStroke(2.dp, Color.White), shadowElevation = 2.dp) {
                                    Text("Đổi ảnh", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }

                        // Tên hiển thị (Text hoặc Input)
                        if (isEditing) {
                            BasicTextField(value = editForm.name, onValueChange = { editForm = editForm.copy(name = it) }, textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 4.dp))
                        } else {
                            Text(text = profile.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 4.dp))
                        }

                        // Vai trò - Căn hộ
                        Text(text = "${profile.role} - Căn hộ ${profile.apartment}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4B5563), modifier = Modifier.padding(bottom = 8.dp))

                        // Badge Trạng thái (Xanh lá cho cư dân)
                        Surface(color = GreenBg, shape = RoundedCornerShape(12.dp)) {
                            Text(text = "Thành viên từ: ${profile.joinDate}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenText, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }

                        // Nút Chỉnh sửa (Chỉ hiện khi không edit)
                        if (!isEditing) {
                            Button(onClick = { startEditing() }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), shape = RoundedCornerShape(12.dp), modifier = Modifier.padding(top = 16.dp).height(40.dp), contentPadding = PaddingValues(horizontal = 24.dp)) {
                                Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Chỉnh sửa", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // --- PHẦN 2: DANH SÁCH FIELD ---
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        val displayProfile = if (isEditing) editForm else profile
                        fieldConfigs.forEach { (key, config) ->
                            val value = when(key) {
                                "fullName" -> displayProfile.fullName; "email" -> displayProfile.email; "phone" -> displayProfile.phone; "gender" -> displayProfile.gender; "idCard" -> displayProfile.idCard; "dob" -> displayProfile.dob; "hometown" -> displayProfile.hometown; "ethnicity" -> displayProfile.ethnicity; "job" -> displayProfile.job; "householdBook" -> displayProfile.householdBook; else -> ""
                            }
                            ProfileDetailItem(
                                label = config.label, value = value, icon = config.icon, iconColor = config.iconColor, iconBg = config.iconBg, isEditing = isEditing, type = config.type, options = config.options,
                                onValueChange = { newValue ->
                                    when(key) {
                                        "fullName" -> editForm = editForm.copy(fullName = newValue); "email" -> editForm = editForm.copy(email = newValue); "phone" -> editForm = editForm.copy(phone = newValue); "gender" -> editForm = editForm.copy(gender = newValue); "idCard" -> editForm = editForm.copy(idCard = newValue); "dob" -> editForm = editForm.copy(dob = newValue); "hometown" -> editForm = editForm.copy(hometown = newValue); "ethnicity" -> editForm = editForm.copy(ethnicity = newValue); "job" -> editForm = editForm.copy(job = newValue); "householdBook" -> editForm = editForm.copy(householdBook = newValue)
                                    }
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    // --- PHẦN 3: NÚT LƯU/HỦY ---
                    if (isEditing) {
                        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = { saveChanges() }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                                Icon(Icons.Default.Save, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Lưu lại", fontWeight = FontWeight.Bold)
                            }
                            Button(onClick = { cancelEditing() }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF374151)), border = BorderStroke(1.dp, Color(0xFFE5E7EB)), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                                Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp)); Spacer(modifier = Modifier.width(8.dp)); Text("Hủy", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Card Bảo mật
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF3F4F6))) {
                Column {
                    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) { Text("Cài đặt tài khoản", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark) }
                    Row(modifier = Modifier.fillMaxWidth().clickable { showPasswordDialog = true }.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).background(Color(0xFFFFF7ED), RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Lock, null, tint = Color(0xFFEA580C), modifier = Modifier.size(20.dp)) }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Đổi mật khẩu", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark, modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF9CA3AF))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (showPasswordDialog) {
            ChangePasswordDialog(onDismiss = { showPasswordDialog = false }, onSave = { showPasswordDialog = false; showSuccessToast = true })
        }
    }
}