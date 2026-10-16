package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.AppTab
import com.example.ui.model.UserSession
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSky
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.HeaderBannerGradient

@Composable
fun AppTopBar(
  session: UserSession,
  onOpenSearch: () -> Unit,
  onQuickAdd: () -> Unit,
  onLogout: () -> Unit,
  onProfileClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(HeaderBannerGradient)
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Brand Title and Icon
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.weight(1f)
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BrandNavyDark)
            .padding(6.dp),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Shield,
            contentDescription = "شعار صناع القادة",
            tint = AccentGold,
            modifier = Modifier.size(28.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "صُنّاع القادة",
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 19.sp
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandSky.copy(alpha = 0.25f)
            ) {
              Text(
                text = "2026",
                color = BrandSky,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Text(
            text = "قاعدة البيانات المركزية والإحصائيات",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Color.White.copy(alpha = 0.85f),
              fontSize = 11.sp
            )
          )
        }
      }

      // Quick Actions
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        // Quick Search Button
        IconButton(
          onClick = onOpenSearch,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.15f))
            .testTag("topbar_search_button")
        ) {
          Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "البحث السريع",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
          )
        }

        // Quick Add Button
        IconButton(
          onClick = onQuickAdd,
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(BrandSky)
            .testTag("topbar_quick_add_button")
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "إضافة سريعة",
            tint = BrandNavyDark,
            modifier = Modifier.size(22.dp)
          )
        }

        // User Profile Role Pill
        Surface(
          shape = RoundedCornerShape(20.dp),
          color = Color.White.copy(alpha = 0.15f),
          modifier = Modifier
            .padding(start = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onProfileClick() }
            .testTag("topbar_profile_chip")
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          ) {
            Box(
              modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(AccentGold)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = session.role.titleArabic.take(8) + "...",
              color = Color.White,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium)
            )
          }
        }
      }
    }
  }
}
