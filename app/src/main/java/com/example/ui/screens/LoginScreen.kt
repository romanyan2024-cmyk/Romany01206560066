package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.UserRole
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSky
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.HeaderBannerGradient
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate200
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.theme.NeutralSlate800
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LoginScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  var username by remember { mutableStateOf("admin") }
  var password by remember { mutableStateOf("••••••") }
  var selectedRole by remember { mutableStateOf(UserRole.ADMIN) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(HeaderBannerGradient),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .clip(RoundedCornerShape(24.dp))
        .verticalScroll(rememberScrollState())
        .padding(vertical = 24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // App Torch Shield Emblem
        Box(
          modifier = Modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(BrandNavyDark),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = null,
            tint = AccentGold,
            modifier = Modifier.size(36.dp)
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "صُنّاع القادة",
          style = MaterialTheme.typography.displayMedium.copy(
            color = BrandNavyDark,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp
          )
        )
        Text(
          text = "قاعدة بيانات 2026 المركزية",
          style = MaterialTheme.typography.titleMedium.copy(
            color = BrandBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
          )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "تسجيل الدخول وإدارة الصلاحيات",
          style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Role Selector Pills
        Text(
          text = "نوع الحساب المسجل:",
          style = MaterialTheme.typography.bodySmall.copy(
            color = NeutralSlate700,
            fontWeight = FontWeight.Bold
          ),
          modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(
            UserRole.ADMIN to "مدير النظام",
            UserRole.SUPERVISOR to "مشرف قطاع",
            UserRole.TEACHER to "خادم مسجل"
          ).forEach { (role, label) ->
            val isSelected = selectedRole == role
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) BrandNavyMedium else NeutralSlate100,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                  selectedRole = role
                  username = when (role) {
                    UserRole.ADMIN -> "admin"
                    UserRole.SUPERVISOR -> "supervisor_minya"
                    UserRole.TEACHER -> "teacher_service"
                  }
                }
            ) {
              Text(
                text = label,
                color = if (isSelected) Color.White else NeutralSlate700,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                  fontSize = 11.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username
        OutlinedTextField(
          value = username,
          onValueChange = { username = it },
          label = { Text("اسم المستخدم / البريد الإلكتروني") },
          leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue) },
          modifier = Modifier.fillMaxWidth().testTag("login_username_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password / PIN
        OutlinedTextField(
          value = password,
          onValueChange = { password = it },
          label = { Text("كلمة المرور أو الرمز السري") },
          leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BrandBlue) },
          visualTransformation = PasswordVisualTransformation(),
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
          modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Login Button
        Button(
          onClick = {
            viewModel.login(username, selectedRole)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("login_submit_button"),
          colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
          shape = RoundedCornerShape(14.dp)
        ) {
          Text(
            text = "تسجيل الدخول للنظام",
            style = MaterialTheme.typography.titleMedium.copy(
              color = Color.White,
              fontWeight = FontWeight.Black
            )
          )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Quick Demo presets
        Text(
          text = "أو اختر تجربة سريعة مباشرة:",
          style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandSkyLight,
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .clickable { viewModel.login("admin", UserRole.ADMIN) }
          ) {
            Text(
              text = "دخول كمدير عام",
              color = BrandNavyDark,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(vertical = 8.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = AccentEmerald.copy(alpha = 0.15f),
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(8.dp))
              .clickable { viewModel.login("supervisor", UserRole.SUPERVISOR) }
          ) {
            Text(
              text = "دخول كمشرف",
              color = AccentEmerald,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
              modifier = Modifier.padding(vertical = 8.dp),
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    }
  }
}
