package com.example.quanlychungcu.ui.screens.auth

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlychungcu.data.api.AuthService
import kotlinx.coroutines.launch

// --- BỘ MÀU INDIGO (Private để không xung đột) ---
private val PrimaryBlue = Color(0xFF4F46E5) // Màu chủ đạo Indigo
private val PrimaryLight = Color(0xFFEEF2FF) // Màu nền icon nhạt
private val TextDark = Color(0xFF1F2937)
private val TextLight = Color(0xFF6B7280)
private val InputBg = Color(0xFFF9FAFB)
private val InputBorder = Color(0xFFE5E7EB)

@Composable
fun LoginScreen(
  onForgotPasswordClick: () -> Unit,
  onLoginSuccess: (String) -> Unit
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var isPasswordVisible by remember { mutableStateOf(false) }

  var errorMessage by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
      .padding(horizontal = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(60.dp))

    // Logo Area
    Box(
      modifier = Modifier
        .size(80.dp)
        .background(PrimaryLight, shape = CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        painter = painterResource(R.drawable.ic_menu_myplaces),
        contentDescription = "Logo",
        tint = PrimaryBlue,
        modifier = Modifier.size(40.dp)
      )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Text(
      text = "Chào mừng trở lại",
      fontSize = 28.sp,
      fontWeight = FontWeight.Bold,
      color = TextDark
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
      text = "Đăng nhập để tiếp tục",
      fontSize = 16.sp,
      color = TextLight
    )

    Spacer(modifier = Modifier.height(48.dp))

    // Username Field
    LoginTextField(
      value = username,
      onValueChange = { username = it },
      placeholder = "Tên đăng nhập",
      iconRes = R.drawable.ic_menu_myplaces
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Password Field
    LoginTextField(
      value = password,
      onValueChange = { password = it },
      placeholder = "Mật khẩu",
      iconRes = R.drawable.ic_lock_idle_lock,
      isPassword = true,
      isPasswordVisible = isPasswordVisible,
      onVisibilityChange = { isPasswordVisible = !isPasswordVisible }
    )

    if (errorMessage.isNotEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))
      Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Forgot Password Link
    Box(
      modifier = Modifier.fillMaxWidth(),
      contentAlignment = Alignment.CenterEnd
    ) {
      Text(
        text = "Quên mật khẩu?",
        color = PrimaryBlue,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.clickable { onForgotPasswordClick() }
      )
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Login Button
    Button(
      onClick = {
        errorMessage = ""
        scope.launch {
          isLoading = true
          try {
            val response = AuthService.login(username, password)
            if (response != null) {
              val userRole = response.user.roleName
              onLoginSuccess(userRole)
            }
            else {
              errorMessage = "Đăng nhập thất bại"
            }
          } catch (e: Exception) {
            errorMessage = e.message ?: "Đăng nhập thất bại"
          } finally {
            isLoading = false
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .shadow(
          elevation = 8.dp,
          shape = RoundedCornerShape(16.dp),
          spotColor = PrimaryBlue.copy(alpha = 0.5f)
        ),
      shape = RoundedCornerShape(16.dp),
      colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          color = Color.White,
          strokeWidth = 2.dp,
          modifier = Modifier.size(20.dp)
        )
      }
      else {
        Text(
          text = "ĐĂNG NHẬP",
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold,
          letterSpacing = 1.sp
        )
      }
    }
    Spacer(modifier = Modifier.weight(1f))
  }
}

// TextField Custom dùng chung cho Login và ForgotPassword
@Composable
fun LoginTextField(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  iconRes: Int,
  isPassword: Boolean = false,
  isPasswordVisible: Boolean = false,
  onVisibilityChange: () -> Unit = {}
) {
  BasicTextField(
    value = value,
    onValueChange = onValueChange,
    singleLine = true,
    visualTransformation = if (isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
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

        if (isPassword) {
          IconButton(onClick = onVisibilityChange) {
            Icon(
              painter = painterResource(R.drawable.ic_menu_view),
              contentDescription = "Toggle View",
              tint = TextLight,
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
  LoginScreen({}, {})
}