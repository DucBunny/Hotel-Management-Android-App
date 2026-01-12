package com.example.quanlychungcu.ui.screens.resident

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.data.model.ReportItem
import com.example.quanlychungcu.ui.components.report.CreateReportDialog
import com.example.quanlychungcu.ui.components.report.DetailReportDialog
import com.example.quanlychungcu.ui.components.report.ReportCard
import com.example.quanlychungcu.ui.theme.*

@Composable
fun ReportScreen(
    reports: List<ReportItem>,
    onAddReport: (String, String, String, ImageBitmap?) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var selectedReport by remember { mutableStateOf<ReportItem?>(null) }

    Box(modifier = Modifier.fillMaxSize().background(BgColor)) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Header
            Column(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, top = 4.dp)) {
                Text("Phản ánh & Kiến nghị", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = TextDark)
                Text("Gửi yêu cầu hỗ trợ tới ban quản lý tòa nhà", style = MaterialTheme.typography.bodyMedium, color = TextGray)
            }

            // Button Tạo mới
            Button(
                onClick = { showCreateDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .height(56.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Filled.AddCircle, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tạo phản ánh mới", fontSize = 16.sp, fontWeight = FontWeight.Bold, maxLines = 1)
                }
            }

            // List Title
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
                Icon(Icons.Outlined.Description, null, tint = TextGray, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lịch sử gửi yêu cầu", fontWeight = FontWeight.Bold, color = TextDark)
            }

            // List Content
            if (reports.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    Text("Chưa có phản ánh nào", color = TextGray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reports) { report ->
                        ReportCard(report = report, onClick = { selectedReport = report })
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateReportDialog(
                onDismiss = { showCreateDialog = false },
                onSubmit = { title, content, type, image ->
                    onAddReport(title, content, type, image)
                    showCreateDialog = false
                }
            )
        }
        if (selectedReport != null) {
            DetailReportDialog(report = selectedReport!!, onDismiss = { selectedReport = null })
        }
    }
}