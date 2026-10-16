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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.NeutralSlate200
import com.example.ui.theme.NeutralSlate500

@Composable
fun MetricCard(
  title: String,
  count: String,
  subtitle: String,
  icon: ImageVector,
  accentColor: Color,
  backgroundColor: Color = Color.White,
  onClick: (() -> Unit)? = null,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
      .border(1.dp, NeutralSlate200, RoundedCornerShape(16.dp))
      .testTag("metric_card_${title.replace(" ", "_")}"),
    colors = CardDefaults.cardColors(containerColor = backgroundColor),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          style = MaterialTheme.typography.bodySmall.copy(
            color = NeutralSlate500,
            fontWeight = FontWeight.Medium
          )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = count,
          style = MaterialTheme.typography.displayMedium.copy(
            color = BrandNavyDark,
            fontWeight = FontWeight.Black,
            fontSize = 24.sp
          )
        )
        if (subtitle.isNotEmpty()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall.copy(
              color = accentColor,
              fontWeight = FontWeight.SemiBold
            )
          )
        }
      }

      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(accentColor.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(24.dp)
        )
      }
    }
  }
}
