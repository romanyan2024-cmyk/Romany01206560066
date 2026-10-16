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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.ui.components.MetricCard
import com.example.ui.model.AppTab
import com.example.ui.model.AreaWithDetails
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.AccentPurple
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
fun DashboardScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val stats by viewModel.dashboardStats.collectAsState()
  val areasWithDetails by viewModel.areasWithDetails.collectAsState()
  val session by viewModel.userSession.collectAsState()

  val minyaAreas = areasWithDetails.filter { it.area.governorateId == "minya" }
  val beniSuefAreas = areasWithDetails.filter { it.area.governorateId == "beni_suef" }

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Welcome & Quick Info Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(HeaderBannerGradient)
          .padding(18.dp)
      ) {
        Column {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "لوحة التحكم الرئيسية 2026",
                style = MaterialTheme.typography.titleLarge.copy(
                  color = Color.White,
                  fontWeight = FontWeight.Black,
                  fontSize = 20.sp
                )
              )
              Text(
                text = "برنامج صُنّاع القادة • محافظتي المنيا وبني سويف",
                style = MaterialTheme.typography.bodySmall.copy(
                  color = BrandSkyLight,
                  fontWeight = FontWeight.Medium
                )
              )
            }

            Surface(
              shape = RoundedCornerShape(20.dp),
              color = Color.White.copy(alpha = 0.2f)
            ) {
              Text(
                text = "مباشر ومحدث",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
              )
            }
          }
        }
      }
    }

    // Quick Add Bar
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NeutralSlate200, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = "إجراءات الإضافة السريعة:",
          style = MaterialTheme.typography.labelMedium.copy(
            color = NeutralSlate700,
            fontWeight = FontWeight.Bold
          )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          QuickActionButton(
            label = "+ طفل",
            color = BrandBlue,
            onClick = { viewModel.showAddChildDialog.value = true },
            modifier = Modifier.weight(1f)
          )
          QuickActionButton(
            label = "+ خادم",
            color = BrandNavyMedium,
            onClick = { viewModel.showAddTeacherDialog.value = true },
            modifier = Modifier.weight(1f)
          )
          QuickActionButton(
            label = "+ فصل",
            color = AccentEmerald,
            onClick = { viewModel.showAddClassDialog.value = true },
            modifier = Modifier.weight(1f)
          )
          QuickActionButton(
            label = "+ مشرف",
            color = AccentPurple,
            onClick = { viewModel.showAddSupervisorDialog.value = true },
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Primary KPI Grid (2 columns)
    Text(
      text = "الأعداد الفعلية والإحصائيات العامة",
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Black,
        color = BrandNavyDark
      )
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      MetricCard(
        title = "إجمالي الأطفال",
        count = "${stats.totalChildren}",
        subtitle = "${stats.boysCount} بنين • ${stats.girlsCount} بنات",
        icon = Icons.Default.Face,
        accentColor = BrandBlue,
        onClick = { viewModel.setTab(AppTab.CHILDREN) },
        modifier = Modifier.weight(1f)
      )
      MetricCard(
        title = "الفصول الدراسية",
        count = "${stats.totalClasses}",
        subtitle = "عبر كل المراحل",
        icon = Icons.Default.School,
        accentColor = AccentEmerald,
        onClick = { viewModel.setTab(AppTab.CLASSES) },
        modifier = Modifier.weight(1f)
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      MetricCard(
        title = "الخدام والمدرسون",
        count = "${stats.totalTeachers}",
        subtitle = "مدربون ومؤهلون",
        icon = Icons.Default.Group,
        accentColor = AccentGold,
        onClick = { viewModel.setTab(AppTab.TEACHERS) },
        modifier = Modifier.weight(1f)
      )
      MetricCard(
        title = "المشرفون",
        count = "${stats.totalSupervisors}",
        subtitle = "${stats.totalAssignments} تكليف بالمناطق",
        icon = Icons.Default.SupervisorAccount,
        accentColor = AccentPurple,
        onClick = { viewModel.setTab(AppTab.SUPERVISORS) },
        modifier = Modifier.weight(1f)
      )
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      MetricCard(
        title = "المناطق المعتمدة",
        count = "${stats.totalAreas}",
        subtitle = "٨ المنيا • ٣ بني سويف",
        icon = Icons.Default.Map,
        accentColor = BrandNavyMedium,
        onClick = { viewModel.setTab(AppTab.AREAS) },
        modifier = Modifier.weight(1f)
      )
      MetricCard(
        title = "المحافظات",
        count = "${stats.totalGovernorates}",
        subtitle = "المنيا وبني سويف",
        icon = Icons.Default.LocationCity,
        accentColor = BrandBlue,
        onClick = { viewModel.setTab(AppTab.AREAS) },
        modifier = Modifier.weight(1f)
      )
    }

    // Demographics Section
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, NeutralSlate200, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "الإحصائيات الديموغرافية للأطفال",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = BrandSkyLight
          ) {
            Text(
              text = "بيانات 2026",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                color = BrandNavyDark
              ),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Gender Distribution
        val totalKids = maxOf(1, stats.totalChildren)
        val boysPercent = (stats.boysCount * 100) / totalKids
        val girlsPercent = 100 - boysPercent

        Text(
          text = "توزيع النوع (بنين وبنات):",
          style = MaterialTheme.typography.bodySmall.copy(
            color = NeutralSlate700,
            fontWeight = FontWeight.Bold
          )
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("بنين: ${stats.boysCount} ($boysPercent%)", style = MaterialTheme.typography.labelMedium.copy(color = BrandBlue, fontWeight = FontWeight.Bold))
          Text("بنات: ${stats.girlsCount} ($girlsPercent%)", style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFFD946EF), fontWeight = FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
          progress = { stats.boysCount.toFloat() / totalKids.toFloat() },
          modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp)),
          color = BrandBlue,
          trackColor = Color(0xFFF0ABFC)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Denominations Distribution
        Text(
          text = "توزيع الطوائف المسجلة:",
          style = MaterialTheme.typography.bodySmall.copy(
            color = NeutralSlate700,
            fontWeight = FontWeight.Bold
          )
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          DenominationStatBox(
            label = "أرثوذكس",
            count = "${stats.orthodoxCount}",
            color = BrandNavyMedium,
            modifier = Modifier.weight(1f)
          )
          DenominationStatBox(
            label = "إنجيلي",
            count = "${stats.evangelicalCount}",
            color = AccentEmerald,
            modifier = Modifier.weight(1f)
          )
          DenominationStatBox(
            label = "كاثوليك",
            count = "${stats.catholicCount}",
            color = AccentGold,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }

    // Minya Governorate Section
    GovernorateHeaderBanner(
      title = "محافظة المنيا (٨ مناطق)",
      subtitle = "إجمالي أطفال المنيا: ${stats.minyaChildrenCount} طفل",
      brush = Brush.horizontalGradient(listOf(BrandNavyDark, Color(0xFF0369A1)))
    )

    minyaAreas.take(4).forEach { areaDetails ->
      AreaCard(
        areaDetails = areaDetails,
        onClick = {
          viewModel.setSelectedAreaId(areaDetails.area.id)
          viewModel.setTab(AppTab.AREAS)
        }
      )
    }

    if (minyaAreas.size > 4) {
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = BrandSkyLight,
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .clickable {
            viewModel.setGovernorateFilter("minya")
            viewModel.setTab(AppTab.AREAS)
          }
      ) {
        Text(
          text = "عرض باقي مناطق المنيا (${minyaAreas.size - 4} مناطق إضافية) ←",
          style = MaterialTheme.typography.bodyMedium.copy(
            color = BrandNavyDark,
            fontWeight = FontWeight.Bold
          ),
          modifier = Modifier.padding(vertical = 10.dp),
          textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
      }
    }

    // Beni Suef Governorate Section
    GovernorateHeaderBanner(
      title = "محافظة بني سويف (٣ مناطق)",
      subtitle = "إجمالي أطفال بني سويف: ${stats.beniSuefChildrenCount} طفل",
      brush = Brush.horizontalGradient(listOf(Color(0xFF0F766E), Color(0xFF0D9488)))
    )

    beniSuefAreas.forEach { areaDetails ->
      AreaCard(
        areaDetails = areaDetails,
        onClick = {
          viewModel.setSelectedAreaId(areaDetails.area.id)
          viewModel.setTab(AppTab.AREAS)
        }
      )
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

@Composable
private fun QuickActionButton(
  label: String,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.12f),
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .clickable { onClick() }
  ) {
    Text(
      text = label,
      color = color,
      style = MaterialTheme.typography.labelMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp
      ),
      modifier = Modifier.padding(vertical = 8.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
  }
}

@Composable
private fun DenominationStatBox(
  label: String,
  count: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = color.copy(alpha = 0.1f),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = count,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Black,
          color = color,
          fontSize = 18.sp
        )
      )
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.SemiBold,
          color = NeutralSlate700
        )
      )
    }
  }
}

@Composable
fun GovernorateHeaderBanner(
  title: String,
  subtitle: String,
  brush: Brush,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(12.dp))
      .background(brush)
      .padding(horizontal = 14.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
          color = Color.White,
          fontWeight = FontWeight.Black,
          fontSize = 15.sp
        )
      )
      Text(
        text = subtitle,
        style = MaterialTheme.typography.bodySmall.copy(
          color = Color.White.copy(alpha = 0.9f),
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
      )
    }
  }
}
