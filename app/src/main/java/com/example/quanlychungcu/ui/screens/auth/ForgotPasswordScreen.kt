package com.example.quanlychungcu.ui.screens.auth

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- MÀU SẮC (Copy từ LoginScreen sang để đồng bộ nhưng là private) ---
private val PrimaryBlue = Color(0xFF4F46E5)
private val PrimaryLight = Color(0xFFEEF2FF)
private val TextDark = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val InputBg = Color(0xFFF9FAFB)
private val InputBorder = Color(0xFFE5E7EB)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Lock Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(PrimaryLight, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_lock_idle_lock),
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quên Mật Khẩu?",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Đừng lo lắng! Hãy nhập email của bạn, chúng tôi sẽ gửi mã đặt lại mật khẩu.",
                fontSize = 15.sp,
                color = TextLight,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Email Input (Dùng lại style của màn hình Login)
            ForgotPasswordTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Nhập địa chỉ Email",
                iconRes = R.drawable.ic_dialog_email
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle logic */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp), spotColor = PrimaryBlue.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text(
                    text = "GỬI MÃ XÁC NHẬN",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

// Copy component TextField sang đây để file hoạt động độc lập
@Composable
fun ForgotPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconRes: Int
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(InputBg, RoundedCornerShape(16.dp))
                    .border(1.dp, InputBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = TextLight,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(placeholder, color = TextLight, fontSize = 15.sp)
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview
@Composable
fun PreviewForgot() {
    ForgotPasswordScreen({})
}