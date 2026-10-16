package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.AreaWithDetails
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSky
import com.example.ui.theme.BrandSkyLight

@Composable
fun AreaCard(
  areaDetails: AreaWithDetails,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Sky blue gradient background
  val cardBrush = Brush.linearGradient(
    colors = listOf(
      Color(0xFFE0F2FE),
      Color(0xFFBAE6FD),
      Color(0xFFE0F7FE)
    )
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onClick() }
      .border(1.dp, Color(0xFF7DD3FC), RoundedCornerShape(16.dp))
      .testTag("area_card_${areaDetails.area.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(cardBrush)
        .padding(16.dp)
    ) {
      Column(modifier = Modifier.fillMaxWidth()) {
        // Area Header & Governorate Badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.White),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = BrandNavyMedium,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = areaDetails.area.name,
                style = MaterialTheme.typography.titleLarge.copy(
                  color = BrandNavyDark,
                  fontWeight = FontWeight.Black,
                  fontSize = 18.sp
                )
              )
              Text(
                text = areaDetails.governorateName,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = BrandNavyMedium,
                  fontWeight = FontWeight.Medium
                )
              )
            }
          }

          // Arrow Action
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color.White.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "عرض التفاصيل",
              tint = BrandNavyDark,
              modifier = Modifier.size(16.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Counts Row: Children, Classes, Teachers
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // Children Pill
          AreaStatPill(
            label = "الأطفال",
            count = "${areaDetails.childrenCount}",
            icon = Icons.Default.Face,
            modifier = Modifier.weight(1f)
          )

          // Classes Pill
          AreaStatPill(
            label = "الفصول",
            count = "${areaDetails.classesCount}",
            icon = Icons.Default.School,
            modifier = Modifier.weight(1f)
          )

          // Teachers Pill
          AreaStatPill(
            label = "الخدام",
            count = "${areaDetails.teachersCount}",
            icon = Icons.Default.Group,
            modifier = Modifier.weight(1f)
          )
        }

        // Supervisor line
        if (areaDetails.supervisors.isNotEmpty()) {
          Spacer(modifier = Modifier.height(10.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.SupervisorAccount,
                contentDescription = null,
                tint = BrandNavyMedium,
                modifier = Modifier.size(14.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "إشراف: ${areaDetails.supervisors.joinToString(" • ") { it.name }}",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = BrandNavyDark,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun AreaStatPill(
  label: String,
  count: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color.White.copy(alpha = 0.95f),
    modifier = modifier
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = BrandNavyMedium,
        modifier = Modifier.size(14.dp)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = count,
          style = MaterialTheme.typography.labelLarge.copy(
            color = BrandNavyDark,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
          )
        )
        Text(
          text = label,
          style = MaterialTheme.typography.labelSmall.copy(
            color = BrandNavyMedium,
            fontSize = 9.sp
          )
        )
      }
    }
  }
}
