package com.example.quanlychungcu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quanlychungcu.ui.screens.auth.LoginScreen
import com.example.quanlychungcu.ui.screens.auth.ForgotPasswordScreen
import com.example.quanlychungcu.ui.screens.resident.ResidentDashboardScreen
import com.example.quanlychungcu.ui.screens.technician.TechnicianDashboardScreen
import com.example.quanlychungcu.ui.theme.QuanLyChungCuTheme

class MainActivity: ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      QuanLyChungCuTheme {
        AppNavigation()
      }
    }
  }
}

@Composable
fun AppNavigation() {
  val navController = rememberNavController()

  NavHost(navController = navController, startDestination = "login") {
    composable("login") {
      LoginScreen(
        onForgotPasswordClick = {
          navController.navigate("forgot_password")
        },
        onLoginSuccess = { role ->
          if (role == "Resident")
            navController.navigate("resident_dashboard") {
              popUpTo("login") {
                inclusive = true
              }
            }
          else if (role == "Technician")
            navController.navigate("technician_dashboard") {
              popUpTo("login") { inclusive = true }
            }
        }
      )
    }
    composable("forgot_password") {
      ForgotPasswordScreen(onBackClick = {
        navController.popBackStack()
      })
    }
    composable("resident_dashboard") {
      ResidentDashboardScreen(onLogout = {
        navController.navigate("login") {
          popUpTo(0) {
            inclusive = true
          }
        }
      })
    }
    composable("technician_dashboard") {
      TechnicianDashboardScreen(onLogout = {
        navController.navigate("login") {
          popUpTo(0) {
            inclusive = true
          }
        }
      })
    }
  }
}
