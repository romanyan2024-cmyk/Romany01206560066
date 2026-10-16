package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.ui.model.AppTab
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate800

@Composable
fun AppNavigation(
  currentTab: AppTab,
  onTabSelected: (AppTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .navigationBarsPadding(),
    color = Color.White,
    shadowElevation = 8.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 8.dp, vertical = 6.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      AppTab.values().forEach { tab ->
        val isSelected = tab == currentTab
        val icon: ImageVector = when (tab) {
          AppTab.DASHBOARD -> Icons.Default.Dashboard
          AppTab.AREAS -> Icons.Default.Map
          AppTab.CHILDREN -> Icons.Default.Face
          AppTab.CLASSES -> Icons.Default.School
          AppTab.TEACHERS -> Icons.Default.Group
          AppTab.SUPERVISORS -> Icons.Default.SupervisorAccount
          AppTab.SEARCH -> Icons.Default.Search
          AppTab.REPORTS -> Icons.Default.Assessment
        }

        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) BrandSkyLight else Color.Transparent)
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("nav_tab_${tab.name.lowercase()}"),
          contentAlignment = Alignment.Center
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = tab.title,
              tint = if (isSelected) BrandNavyMedium else NeutralSlate500,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = tab.title,
              style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) BrandNavyDark else NeutralSlate800,
                fontSize = 12.sp
              )
            )
          }
        }
      }
    }
  }
}
