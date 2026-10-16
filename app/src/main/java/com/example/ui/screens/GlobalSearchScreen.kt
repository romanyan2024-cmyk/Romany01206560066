package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate200
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.viewmodel.MainViewModel

@Composable
fun GlobalSearchScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val searchQuery by viewModel.searchQuery.collectAsState()
  val filterType by viewModel.searchFilterType.collectAsState()
  val results by viewModel.searchResults.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    // Header
    Text(
      text = "البحث العام الشامل",
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Black,
        color = BrandNavyDark
      )
    )
    Text(
      text = "بحث فوري في الأطفال، الخدام، الفصول، المشرفين، والمناطق",
      style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Prominent Search Input
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { viewModel.setSearchQuery(it) },
      label = { Text("اكتب كلمة البحث (اسم، هاتف، منطقة، تخصص...)") },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = null, tint = BrandBlue)
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { viewModel.setSearchQuery("") }) {
            Icon(Icons.Default.Clear, contentDescription = "مسح", tint = NeutralSlate500)
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("global_search_input"),
      singleLine = true,
      shape = RoundedCornerShape(14.dp)
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Filter Type Chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState()),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      SearchResultType.values().forEach { type ->
        val isSelected = filterType == type
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isSelected) BrandNavyDark else NeutralSlate100,
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { viewModel.setSearchFilterType(type) }
        ) {
          Text(
            text = type.label,
            color = if (isSelected) Color.White else NeutralSlate700,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            ),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Results Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = if (searchQuery.isBlank()) "كافة السجلات المتاحة" else "نتائج البحث المطابقة",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark)
      )
      Surface(shape = RoundedCornerShape(8.dp), color = BrandSkyLight) {
        Text(
          text = "${results.size} نتيجة",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark),
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Results List
    if (results.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(Icons.Default.Search, contentDescription = null, tint = NeutralSlate500, modifier = Modifier.size(54.dp))
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "لا توجد نتائج مطابقة لبحثك",
            style = MaterialTheme.typography.titleMedium.copy(color = BrandNavyDark, fontWeight = FontWeight.Bold)
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "تأكد من كتابة الكلمة بدون أخطاء إملائية أو اختر فئة 'الكل'",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
          )
        }
      }
    } else {
      LazyColumn(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(results, key = { "${it.type}_${it.id}" }) { item ->
          SearchResultCard(
            item = item,
            onClick = { viewModel.showDetail(item) }
          )
        }

        item {
          Spacer(modifier = Modifier.height(30.dp))
        }
      }
    }
  }
}

@Composable
private fun SearchResultCard(
  item: SearchResultItem,
  onClick: () -> Unit
) {
  val icon: ImageVector
  val iconBg: Color
  val iconTint: Color

  when (item.type) {
    SearchResultType.CHILD -> {
      icon = Icons.Default.Face
      iconBg = BrandSkyLight
      iconTint = BrandBlue
    }
    SearchResultType.TEACHER -> {
      icon = Icons.Default.Group
      iconBg = Color(0xFFFEF3C7)
      iconTint = AccentGold
    }
    SearchResultType.SUPERVISOR -> {
      icon = Icons.Default.SupervisorAccount
      iconBg = Color(0xFFEDE9FE)
      iconTint = AccentPurple
    }
    SearchResultType.CLASS -> {
      icon = Icons.Default.School
      iconBg = Color(0xFFD1FAE5)
      iconTint = AccentEmerald
    }
    SearchResultType.AREA -> {
      icon = Icons.Default.LocationOn
      iconBg = NeutralSlate100
      iconTint = BrandNavyMedium
    }
    else -> {
      icon = Icons.Default.Face
      iconBg = BrandSkyLight
      iconTint = BrandBlue
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .clickable { onClick() }
      .border(1.dp, NeutralSlate200, RoundedCornerShape(12.dp))
      .testTag("search_result_${item.id}"),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(iconBg),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(20.dp)
          )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = item.title,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = BrandNavyDark,
                fontSize = 15.sp
              )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Surface(shape = RoundedCornerShape(4.dp), color = NeutralSlate100) {
              Text(
                text = item.type.label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = NeutralSlate700),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = item.subtitle,
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500, fontSize = 12.sp),
            maxLines = 1
          )
          if (item.locationInfo.isNotBlank()) {
            Text(
              text = item.locationInfo,
              style = MaterialTheme.typography.bodySmall.copy(color = BrandBlue, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            )
          }
        }
      }

      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "عرض التفاصيل",
        tint = NeutralSlate500,
        modifier = Modifier.size(16.dp)
      )
    }
  }
}
