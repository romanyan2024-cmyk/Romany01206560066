package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AreaCard
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate200
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.theme.NeutralSlate800
import com.example.ui.viewmodel.MainViewModel

@Composable
fun AreasBrowserScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val areasWithDetails by viewModel.areasWithDetails.collectAsState()
  val selectedAreaId by viewModel.selectedAreaId.collectAsState()
  val govFilter by viewModel.selectedGovernorateFilter.collectAsState()

  val children by viewModel.childrenWithDetails.collectAsState()
  val teachers by viewModel.teachersWithDetails.collectAsState()
  val classes by viewModel.classesWithDetails.collectAsState()

  // Handle Android back button when viewing an area's drilldown
  if (selectedAreaId != null) {
    BackHandler {
      viewModel.setSelectedAreaId(null)
    }
  }

  val selectedAreaDetails = areasWithDetails.find { it.area.id == selectedAreaId }

  if (selectedAreaDetails != null) {
    // Area Detailed View (Drilldown)
    val areaChildren = children.filter { it.child.areaId == selectedAreaDetails.area.id }
    val areaTeachers = teachers.filter { it.teacher.areaId == selectedAreaDetails.area.id }
    val areaClasses = classes.filter { it.classEntity.areaId == selectedAreaDetails.area.id }

    var detailTab by remember { mutableStateOf("children") } // "children", "classes", "teachers"

    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
      // Top Navigation back
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { viewModel.setSelectedAreaId(null) },
          modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(NeutralSlate100)
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "الرجوع للمناطق",
            tint = BrandNavyDark
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = "منطقة ${selectedAreaDetails.area.name}",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Text(
            text = selectedAreaDetails.governorateName,
            style = MaterialTheme.typography.bodySmall.copy(
              color = BrandNavyMedium,
              fontWeight = FontWeight.Medium
            )
          )
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Area Highlights Banner
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrandSkyLight),
        shape = RoundedCornerShape(14.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("${selectedAreaDetails.childrenCount}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark))
              Text("أطفال", style = MaterialTheme.typography.labelSmall.copy(color = NeutralSlate700))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("${selectedAreaDetails.classesCount}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark))
              Text("فصول", style = MaterialTheme.typography.labelSmall.copy(color = NeutralSlate700))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text("${selectedAreaDetails.teachersCount}", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark))
              Text("خدام ومدرسون", style = MaterialTheme.typography.labelSmall.copy(color = NeutralSlate700))
            }
          }

          if (selectedAreaDetails.supervisors.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "المشرف المتابع: " + selectedAreaDetails.supervisors.joinToString("، ") { it.name },
              style = MaterialTheme.typography.bodySmall.copy(color = BrandNavyMedium, fontWeight = FontWeight.Bold)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Section Tabs: Children / Classes / Teachers
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        SubTabPill(
          title = "الأطفال (${areaChildren.size})",
          isSelected = detailTab == "children",
          onClick = { detailTab = "children" },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = "الفصول (${areaClasses.size})",
          isSelected = detailTab == "classes",
          onClick = { detailTab = "classes" },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = "الخدام (${areaTeachers.size})",
          isSelected = detailTab == "teachers",
          onClick = { detailTab = "teachers" },
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Content List based on selected sub-tab
      when (detailTab) {
        "children" -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(areaChildren, key = { it.child.id }) { item ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    viewModel.showDetail(
                      SearchResultItem(
                        id = item.child.id,
                        type = SearchResultType.CHILD,
                        title = item.child.name,
                        subtitle = "الفصل: ${item.className} • ${item.child.denomination}",
                        locationInfo = "${item.governorateName} - ${item.areaName}",
                        tag = item.child.gender,
                        childRef = item.child
                      )
                    )
                  }
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
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(if (item.child.gender == "بنين") BrandSkyLight else Color(0xFFFCE7F3)),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = if (item.child.gender == "بنين") BrandBlue else Color(0xFFDB2777),
                        modifier = Modifier.size(20.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(item.child.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                      Text("${item.className} • ${item.child.denomination}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
                    }
                  }
                  Surface(shape = RoundedCornerShape(6.dp), color = NeutralSlate100) {
                    Text(
                      text = "${2026 - item.child.birthYear} سنوات",
                      modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                      style = MaterialTheme.typography.labelSmall
                    )
                  }
                }
              }
            }
          }
        }

        "classes" -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(areaClasses, key = { it.classEntity.id }) { item ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    viewModel.showDetail(
                      SearchResultItem(
                        id = item.classEntity.id,
                        type = SearchResultType.CLASS,
                        title = item.classEntity.name,
                        subtitle = "المسؤول: ${item.teacherName} • ${item.childrenCount} طفل",
                        locationInfo = "${item.governorateName} - ${item.areaName}",
                        tag = item.classEntity.stage,
                        classRef = item.classEntity
                      )
                    )
                  }
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
                    Text(item.classEntity.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Text("المدرس المسؤول: ${item.teacherName}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
                  }
                  Surface(shape = RoundedCornerShape(6.dp), color = AccentEmerald.copy(alpha = 0.15f)) {
                    Text(
                      text = "${item.childrenCount} طفل",
                      color = AccentEmerald,
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
              }
            }
          }
        }

        "teachers" -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            items(areaTeachers, key = { it.teacher.id }) { item ->
              Card(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(12.dp))
                  .clickable {
                    viewModel.showDetail(
                      SearchResultItem(
                        id = item.teacher.id,
                        type = SearchResultType.TEACHER,
                        title = item.teacher.name,
                        subtitle = "التخصص: ${item.teacher.specialty}",
                        locationInfo = item.areaName,
                        tag = "خادم",
                        teacherRef = item.teacher
                      )
                    )
                  }
                  .border(1.dp, NeutralSlate200, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White)
              ) {
                Column(modifier = Modifier.padding(12.dp)) {
                  Text(item.teacher.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                  Text(item.teacher.specialty, style = MaterialTheme.typography.bodySmall.copy(color = BrandNavyMedium))
                  if (item.teacher.phone.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("هاتف: ${item.teacher.phone}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
                  }
                }
              }
            }
          }
        }
      }
    }
  } else {
    // Areas Master List
    val filteredAreas = when (govFilter) {
      "minya" -> areasWithDetails.filter { it.area.governorateId == "minya" }
      "beni_suef" -> areasWithDetails.filter { it.area.governorateId == "beni_suef" }
      else -> areasWithDetails
    }

    LazyColumn(
      modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      item {
        Column {
          Text(
            text = "استعراض المناطق والمحافظات",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Text(
            text = "استعراض الهيكل الجغرافي لبرنامج صناع القادة 2026",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Filter Row
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            GovFilterChip(
              title = "جميع المناطق (١١)",
              isSelected = govFilter == "all",
              onClick = { viewModel.setGovernorateFilter("all") },
              modifier = Modifier.weight(1f)
            )
            GovFilterChip(
              title = "المنيا (٨)",
              isSelected = govFilter == "minya",
              onClick = { viewModel.setGovernorateFilter("minya") },
              modifier = Modifier.weight(1f)
            )
            GovFilterChip(
              title = "بني سويف (٣)",
              isSelected = govFilter == "beni_suef",
              onClick = { viewModel.setGovernorateFilter("beni_suef") },
              modifier = Modifier.weight(1f)
            )
          }
        }
      }

      items(filteredAreas, key = { it.area.id }) { areaDetails ->
        AreaCard(
          areaDetails = areaDetails,
          onClick = { viewModel.setSelectedAreaId(areaDetails.area.id) }
        )
      }

      item {
        Spacer(modifier = Modifier.height(30.dp))
      }
    }
  }
}

@Composable
private fun GovFilterChip(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = if (isSelected) BrandNavyDark else NeutralSlate100,
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
  ) {
    Text(
      text = title,
      color = if (isSelected) Color.White else NeutralSlate700,
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        fontSize = 11.sp
      ),
      modifier = Modifier.padding(vertical = 10.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

@Composable
private fun SubTabPill(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isSelected) BrandBlue else NeutralSlate100,
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
  ) {
    Text(
      text = title,
      color = if (isSelected) Color.White else NeutralSlate700,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold
      ),
      modifier = Modifier.padding(vertical = 8.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}
