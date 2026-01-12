package com.example.quanlychungcu.ui.components.report

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.quanlychungcu.data.model.ReportItem
import com.example.quanlychungcu.ui.theme.*

@Composable
fun ReportCard(
    report: ReportItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        border = BorderStroke(1.dp, Color(0xFFF3F4F6)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = OrangeBg,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFFFEDD5))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).background(OrangeWarning, CircleShape))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ĐANG CHỜ XỬ LÝ", color = OrangeText, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(report.time, fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.Medium)
                    Text(" | ", fontSize = 11.sp, color = Color.LightGray)
                    Text(report.date, fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.Medium)
                }
            }

            HorizontalDivider(color = Color(0xFFF3F4F6), modifier = Modifier.padding(vertical = 16.dp))

            Text(report.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))
            Text(report.content, style = MaterialTheme.typography.bodySmall, color = Color(0xFF4B5563), maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 18.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateReportDialog(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, ImageBitmap?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var capturedBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            capturedBitmap = bitmap.asImageBitmap()
        }
    }

    val jobTypes = listOf("Sửa chữa điện", "Sửa ống nước", "Thiết bị chung cư", "Khác")

    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.95f).padding(vertical = 24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tạo phản ánh", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp).background(Color(0xFFE5E7EB), CircleShape)) {
                        Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = Color(0xFF4B5563))
                    }
                }
                HorizontalDivider(color = Color(0xFFF3F4F6))

                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Tiêu đề", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))
                    OutlinedTextField(
                        value = title, onValueChange = { title = it },
                        placeholder = { Text("Ví dụ: Hỏng đèn") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = BluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Nội dung", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))
                    OutlinedTextField(
                        value = content, onValueChange = { content = it },
                        placeholder = { Text("Mô tả chi tiết...") },
                        modifier = Modifier.fillMaxWidth().height(100.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = BluePrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Loại công việc", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                            .clickable { expanded = true }
                            .padding(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(if (selectedType.isEmpty()) "Chọn công việc" else selectedType, color = if(selectedType.isEmpty()) TextGray else TextDark)
                            Icon(Icons.Default.ArrowDropDown, null, tint = TextGray)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            jobTypes.forEach { type ->
                                DropdownMenuItem(text = { Text(type) }, onClick = { selectedType = type; expanded = false })
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Hình ảnh đính kèm", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 6.dp))

                    if (capturedBitmap != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                bitmap = capturedBitmap!!,
                                contentDescription = "Captured Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { capturedBitmap = null },
                                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).size(32.dp).background(Color.White.copy(alpha = 0.8f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, null, tint = RedError, modifier = Modifier.size(18.dp))
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFEFF6FF))
                                .drawBehind {
                                    val stroke = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                                    drawRoundRect(color = Color(0xFFBFDBFE), style = stroke, cornerRadius = CornerRadius(12.dp.toPx()))
                                }
                                .clickable { cameraLauncher.launch(null) },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CameraAlt, null, tint = BluePrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Chụp ảnh", color = BluePrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }

                Row(modifier = Modifier.padding(20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = TextDark), border = BorderStroke(1.dp, Color(0xFFE5E7EB)), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Hủy", fontWeight = FontWeight.Bold)
                    }
                    Button(onClick = { onSubmit(title, content, selectedType, capturedBitmap) }, colors = ButtonDefaults.buttonColors(containerColor = BluePrimary), shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f).height(48.dp)) {
                        Text("Gửi yêu cầu", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun DetailReportDialog(
    report: ReportItem,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth(0.95f).padding(vertical = 24.dp)
        ) {
            Column {
                Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Chi tiết phản ánh", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp).background(Color(0xFFE5E7EB), CircleShape)) {
                        Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp), tint = Color(0xFF4B5563))
                    }
                }
                HorizontalDivider(color = Color(0xFFF3F4F6))

                Column(modifier = Modifier.padding(24.dp)) {
                    Text(report.title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = TextGray, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${report.time} - ${report.date}", fontSize = 12.sp, color = TextGray)
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFF9FAFB), RoundedCornerShape(12.dp))
                            .border(1.dp, Color(0xFFF3F4F6), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Text("NỘI DUNG", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(report.content, fontSize = 14.sp, color = TextDark, lineHeight = 22.sp)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (report.capturedImage != null) {
                        Text("HÌNH ẢNH ĐÍNH KÈM", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        ) {
                            Image(
                                bitmap = report.capturedImage!!,
                                contentDescription = "Detail Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).drawBehind {
                        drawLine(color = Color(0xFFF3F4F6), start = androidx.compose.ui.geometry.Offset(0f, 0f), end = androidx.compose.ui.geometry.Offset(size.width, 0f), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                    })

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Loại công việc", fontSize = 14.sp, color = TextGray)
                        Text(report.type, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Trạng thái", fontSize = 14.sp, color = TextGray)
                        Surface(color = OrangeBg, shape = RoundedCornerShape(50), border = BorderStroke(1.dp, Color(0xFFFFEDD5))) {
                            Text("ĐANG CHỜ XỬ LÝ", color = OrangeText, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                        }
                    }
                }

                Box(modifier = Modifier.padding(20.dp)) {
                    Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = TextDark), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().height(48.dp)) {
                        Text("Đóng", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}