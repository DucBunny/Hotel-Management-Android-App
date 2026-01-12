package com.example.quanlychungcu.ui.components.profile

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quanlychungcu.ui.theme.*
import java.util.Calendar

// --- 1. DATA CLASS CẤU HÌNH TRƯỜNG TIN (DÙNG CHUNG) ---
data class FieldConfig(
    val label: String,
    val icon: ImageVector,
    val iconColor: Color,
    val iconBg: Color,
    val type: String = "text", // "text", "select", "date"
    val options: List<String> = emptyList()
)

// --- 2. COMPONENT HIỂN THỊ DÒNG THÔNG TIN ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailItem(
    label: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    iconBg: Color,
    isEditing: Boolean,
    type: String,
    options: List<String> = emptyList(),
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val d = if (day < 10) "0$day" else "$day"
            val m = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
            onValueChange("$d/$m/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, RoundedCornerShape(8.dp))
                .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(18.dp))
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = TextGray, modifier = Modifier.padding(bottom = 2.dp))

            if (isEditing) {
                when (type) {
                    "select" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
                                .clickable { expanded = true }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                                Icon(Icons.Default.ArrowDropDown, null, tint = TextGray, modifier = Modifier.size(16.dp))
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                                options.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            onValueChange(option)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    "date" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
                                .clickable { datePickerDialog.show() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
                        }
                    }
                    else -> {
                        BasicTextField(
                            value = value,
                            onValueChange = onValueChange,
                            textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White, RoundedCornerShape(4.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// --- 3. DIALOG ĐỔI MẬT KHẨU ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showCurrent by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.95f)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Đổi mật khẩu", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp).background(Color(0xFFE5E7EB), CircleShape)) {
                        Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = Color(0xFF4B5563))
                    }
                }
                HorizontalDivider(color = Color(0xFFF3F4F6))
                Column(modifier = Modifier.padding(20.dp)) {
                    PasswordInput("Mật khẩu hiện tại", currentPassword, showCurrent, { currentPassword = it }, { showCurrent = !showCurrent })
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordInput("Mật khẩu mới", newPassword, showNew, { newPassword = it }, { showNew = !showNew })
                    Spacer(modifier = Modifier.height(16.dp))
                    PasswordInput("Nhập lại mật khẩu mới", confirmPassword, showConfirm, { confirmPassword = it }, { showConfirm = !showConfirm })
                }
                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark), border = BorderStroke(1.dp, Color(0xFFE5E7EB)), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Hủy", fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Lưu thay đổi", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun PasswordInput(label: String, value: String, isVisible: Boolean, onValueChange: (String) -> Unit, onToggleVisibility: () -> Unit) {
    Column {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))
        OutlinedTextField(
            value = value, onValueChange = onValueChange, placeholder = { Text("••••••••", fontSize = 14.sp, color = Color.Gray) },
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { IconButton(onClick = onToggleVisibility) { Icon(if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = Color.Gray) } },
            modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color(0xFFE5E7EB), focusedBorderColor = BluePrimary)
        )
    }
}