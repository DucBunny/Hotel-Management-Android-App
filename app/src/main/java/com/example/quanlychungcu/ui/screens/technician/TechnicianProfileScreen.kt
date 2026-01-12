package com.example.quanlychungcu.ui.screens.technician

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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

// --- CÁC IMPORT QUAN TRỌNG ĐÃ ĐƯỢC SỬA ---
import com.example.quanlychungcu.ui.components.profile.FieldConfig
import com.example.quanlychungcu.ui.components.profile.ProfileDetailItem
import com.example.quanlychungcu.ui.components.profile.ChangePasswordDialog

// MODEL CỤC BỘ CHO UI KỸ THUẬT
data class TechnicianProfile(
    var name: String,
    val role: String,
    val employeeId: String,
    val department: String,
    // Details
    var fullName: String,
    var email: String,
    var phone: String,
    var address: String,
    var specialty: String,
    var shift: String
)

@Composable
fun TechnicianProfileScreen() {
    var isEditing by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showSuccessToast by remember { mutableStateOf(false) }

    // DỮ LIỆU MẪU KỸ THUẬT VIÊN
    var profile by remember { mutableStateOf(
        TechnicianProfile(
            name = "Trần Văn B",
            role = "Kỹ thuật viên",
            employeeId = "NV-0824",
            department = "Bảo trì điện",
            fullName = "Trần Văn B",
            email = "tranvanb@tech.com",
            phone = "0912 345 678",
            address = "Thanh Xuân, Hà Nội",
            specialty = "Hệ thống điện nhẹ",
            shift = "Ca sáng (08:00 - 17:00)"
        )
    )}
    var editForm by remember { mutableStateOf(profile.copy()) }

    // CẤU HÌNH FIELD CHO KỸ THUẬT VIÊN
    val fieldConfigs = mapOf(
        "fullName" to FieldConfig("Họ và tên", Icons.Default.Person, BluePrimary, Color(0xFFEFF6FF)),
        "employeeId" to FieldConfig("Mã nhân viên", Icons.Default.Badge, IndigoPrimary, Color(0xFFEEF2FF)),
        "department" to FieldConfig("Bộ phận", Icons.Default.Business, Color(0xFF059669), Color(0xFFECFDF5)),
        "specialty" to FieldConfig("Chuyên môn", Icons.Default.Build, OrangeWarning, Color(0xFFFFF7ED)),
        "shift" to FieldConfig("Ca làm việc", Icons.Default.AccessTime, Color(0xFFDB2777), Color(0xFFFDF2F8)),
        "phone" to FieldConfig("Số điện thoại", Icons.Default.Phone, GreenSuccess, Color(0xFFF0FDF4)),
        "email" to FieldConfig("Email", Icons.Default.Email, AmberText, Color(0xFFFFF7ED)),
        "address" to FieldConfig("Địa chỉ liên hệ", Icons.Default.Place, RedError, Color(0xFFFEF2F2))
    )

    fun startEditing() { editForm = profile.copy(); isEditing = true }
    fun cancelEditing() { isEditing = false }
    fun saveChanges() { profile = editForm.copy(); isEditing = false }

    LaunchedEffect(showSuccessToast) { if (showSuccessToast) { delay(3000); showSuccessToast = false } }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        // TOAST
        AnimatedVisibility(visible = showSuccessToast, enter = slideInVertically { -it } + fadeIn(), exit = slideOutVertically { -it } + fadeOut(), modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp).zIndex(100f)) {
            Surface(color = GreenBg, border = BorderStroke(1.dp, GreenBorder), shape = RoundedCornerShape(12.dp), shadowElevation = 4.dp) {
                Row(modifier = Modifier.padding(16.dp)) { Icon(Icons.Default.CheckCircle, null, tint = GreenSuccess); Spacer(Modifier.width(8.dp)); Text("Thành công!", color = GreenText, fontWeight = FontWeight.Bold) }
            }
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            // Header
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp, top = 4.dp)) {
                Text(if (isEditing) "Cập nhật thông tin" else "Hồ sơ nhân viên", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Thông tin làm việc và liên hệ", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }

            // Card
            Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF3F4F6))) {
                Column(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.padding(bottom = 16.dp)) {
                        Box(modifier = Modifier.size(96.dp).background(Color(0xFFEFF6FF), CircleShape).border(4.dp, Color.White, CircleShape), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Engineering, null, tint = BluePrimary, modifier = Modifier.size(48.dp))
                        }
                    }
                    if (isEditing) {
                        BasicTextField(value = editForm.name, onValueChange = { editForm = editForm.copy(name = it) }, textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark, textAlign = TextAlign.Center), modifier = Modifier.fillMaxWidth(0.7f).padding(bottom = 4.dp))
                    } else {
                        Text(profile.name, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    }
                    Text("${profile.role} - ${profile.employeeId}", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))

                    if (!isEditing) {
                        Button(onClick = { startEditing() }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp)); Spacer(Modifier.width(8.dp)); Text("Chỉnh sửa")
                        }
                    }
                }

                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    val displayProfile = if (isEditing) editForm else profile
                    fieldConfigs.forEach { (key, config) ->
                        val value = when(key) {
                            "fullName" -> displayProfile.fullName; "employeeId" -> displayProfile.employeeId; "department" -> displayProfile.department; "specialty" -> displayProfile.specialty; "shift" -> displayProfile.shift; "phone" -> displayProfile.phone; "email" -> displayProfile.email; "address" -> displayProfile.address; else -> ""
                        }
                        ProfileDetailItem(label = config.label, value = value, icon = config.icon, iconColor = config.iconColor, iconBg = config.iconBg, isEditing = isEditing, type = config.type, onValueChange = { newValue ->
                            when(key) { "fullName" -> editForm = editForm.copy(fullName = newValue); "phone" -> editForm = editForm.copy(phone = newValue); "address" -> editForm = editForm.copy(address = newValue); "email" -> editForm = editForm.copy(email = newValue) }
                        })
                    }
                }

                if (isEditing) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(onClick = { saveChanges() }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), modifier = Modifier.weight(1f)) { Text("Lưu lại") }
                        Button(onClick = { cancelEditing() }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black), border = BorderStroke(1.dp, Color.LightGray), modifier = Modifier.weight(1f)) { Text("Hủy") }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            // Security
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF3F4F6))) {
                Row(modifier = Modifier.fillMaxWidth().clickable { showPasswordDialog = true }.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, null, tint = Color(0xFFEA580C)); Spacer(Modifier.width(12.dp)); Text("Đổi mật khẩu", fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f)); Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                }
            }
        }
        if (showPasswordDialog) ChangePasswordDialog({ showPasswordDialog = false }, { showPasswordDialog = false; showSuccessToast = true })
    }
}