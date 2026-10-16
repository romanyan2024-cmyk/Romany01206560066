package com.example.ui.screens

import android.content.Intent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate200
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val areasWithDetails by viewModel.areasWithDetails.collectAsState()
  val governorates by viewModel.governorates.collectAsState()

  var selectedGovFilter by remember { mutableStateOf("all") }
  var selectedAreaFilter by remember { mutableStateOf("all") }

  var govMenuExpanded by remember { mutableStateOf(false) }
  var areaMenuExpanded by remember { mutableStateOf(false) }

  val filteredAreas = areasWithDetails.filter { item ->
    val matchesGov = selectedGovFilter == "all" || item.area.governorateId == selectedGovFilter
    val matchesArea = selectedAreaFilter == "all" || item.area.id == selectedAreaFilter
    matchesGov && matchesArea
  }

  val totalChildren = filteredAreas.sumOf { it.childrenCount }
  val totalBoys = filteredAreas.sumOf { it.boysCount }
  val totalGirls = filteredAreas.sumOf { it.girlsCount }
  val totalClasses = filteredAreas.sumOf { it.classesCount }
  val totalTeachers = filteredAreas.sumOf { it.teachersCount }

  val shareReport = {
    val reportText = buildString {
      appendLine("📊 *تقرير إحصائيات برنامج صُنّاع القادة 2026*")
      appendLine("-----------------------------------")
      appendLine("المحافظة: ${if (selectedGovFilter == "all") "جميع المحافظات" else governorates.find { it.id == selectedGovFilter }?.name}")
      appendLine("المنطقة: ${if (selectedAreaFilter == "all") "جميع المناطق (${filteredAreas.size})" else filteredAreas.firstOrNull()?.area?.name}")
      appendLine("👥 إجمالي الأطفال: $totalChildren ($totalBoys بنين • $totalGirls بنات)")
      appendLine("🏫 إجمالي الفصول: $totalClasses")
      appendLine("👨‍🏫 إجمالي الخدام والمدرسين: $totalTeachers")
      appendLine("-----------------------------------")
      appendLine("تفاصيل المناطق:")
      filteredAreas.forEach { a ->
        appendLine("• ${a.area.name} (${a.governorateName}): ${a.childrenCount} طفل | ${a.classesCount} فصول | ${a.teachersCount} خدام")
      }
      appendLine("-----------------------------------")
      appendLine("تم الاستخراج من قاعدة بيانات صُنّاع القادة 2026")
    }

    val sendIntent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_TEXT, reportText)
      type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "مشاركة التقرير عبر:"))
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "التقارير والإحصائيات",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            color = BrandNavyDark
          )
        )
        Text(
          text = "استخراج المجاميع والتوزيعات الجغرافية",
          style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
        )
      }

      Button(
        onClick = shareReport,
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.testTag("share_report_button")
      ) {
        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text("مشاركة")
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Filters Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Governorate Filter Dropdown
      Box(modifier = Modifier.weight(1f)) {
        val govName = when (selectedGovFilter) {
          "minya" -> "المنيا"
          "beni_suef" -> "بني سويف"
          else -> "كافة المحافظات"
        }
        OutlinedTextField(
          value = govName,
          onValueChange = {},
          readOnly = true,
          label = { Text("المحافظة") },
          modifier = Modifier.fillMaxWidth().clickable { govMenuExpanded = true }
        )
        DropdownMenu(expanded = govMenuExpanded, onDismissRequest = { govMenuExpanded = false }) {
          DropdownMenuItem(
            text = { Text("كافة المحافظات") },
            onClick = {
              selectedGovFilter = "all"
              selectedAreaFilter = "all"
              govMenuExpanded = false
            }
          )
          governorates.forEach { gov ->
            DropdownMenuItem(
              text = { Text(gov.name) },
              onClick = {
                selectedGovFilter = gov.id
                selectedAreaFilter = "all"
                govMenuExpanded = false
              }
            )
          }
        }
      }

      // Area Filter Dropdown
      Box(modifier = Modifier.weight(1f)) {
        val availableAreas = if (selectedGovFilter == "all") areasWithDetails else areasWithDetails.filter { it.area.governorateId == selectedGovFilter }
        val areaName = if (selectedAreaFilter == "all") "كافة المناطق" else availableAreas.find { it.area.id == selectedAreaFilter }?.area?.name ?: "كافة المناطق"

        OutlinedTextField(
          value = areaName,
          onValueChange = {},
          readOnly = true,
          label = { Text("المنطقة") },
          modifier = Modifier.fillMaxWidth().clickable { areaMenuExpanded = true }
        )
        DropdownMenu(expanded = areaMenuExpanded, onDismissRequest = { areaMenuExpanded = false }) {
          DropdownMenuItem(
            text = { Text("كافة المناطق") },
            onClick = {
              selectedAreaFilter = "all"
              areaMenuExpanded = false
            }
          )
          availableAreas.forEach { a ->
            DropdownMenuItem(
              text = { Text(a.area.name) },
              onClick = {
                selectedAreaFilter = a.area.id
                areaMenuExpanded = false
              }
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Summary Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NeutralSlate200, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = BrandSkyLight)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "إجمالي التقرير المحدد:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          ReportPill("الأطفال", "$totalChildren", BrandBlue)
          ReportPill("بنين", "$totalBoys", BrandBlue)
          ReportPill("بنات", "$totalGirls", Color(0xFFD946EF))
          ReportPill("الفصول", "$totalClasses", AccentEmerald)
          ReportPill("الخدام", "$totalTeachers", AccentGold)
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "جدول توزيع المناطق (${filteredAreas.size} منطقة):",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark)
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyColumn(
      modifier = Modifier.weight(1f),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(filteredAreas, key = { it.area.id }) { item ->
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, NeutralSlate200, RoundedCornerShape(12.dp)),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(item.area.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark))
              Text(item.governorateName, style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              BadgeItem("${item.childrenCount} طفل", BrandSkyLight, BrandNavyDark)
              BadgeItem("${item.classesCount} فصول", NeutralSlate100, NeutralSlate700)
              BadgeItem("${item.teachersCount} خدام", NeutralSlate100, NeutralSlate700)
            }
          }
        }
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
private fun ReportPill(title: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = color, fontSize = 18.sp))
    Text(title, style = MaterialTheme.typography.labelSmall.copy(color = NeutralSlate700, fontSize = 10.sp))
  }
}

@Composable
private fun BadgeItem(text: String, bg: Color, textColor: Color) {
  Surface(shape = RoundedCornerShape(6.dp), color = bg) {
    Text(text, color = textColor, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
  }
}
