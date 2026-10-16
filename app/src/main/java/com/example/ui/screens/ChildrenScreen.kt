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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildEntity
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentCoral
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
fun ChildrenScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val children by viewModel.childrenWithDetails.collectAsState()
  val areas by viewModel.areas.collectAsState()

  var searchQuery by remember { mutableStateOf("") }
  var selectedGender by remember { mutableStateOf("الكل") }
  var selectedDenom by remember { mutableStateOf("الكل") }
  var selectedAreaFilter by remember { mutableStateOf("all") }
  var childToDelete by remember { mutableStateOf<ChildEntity?>(null) }

  val filteredChildren = children.filter { item ->
    val matchesQuery = searchQuery.isBlank() ||
      item.child.name.contains(searchQuery, ignoreCase = true) ||
      item.child.guardianName.contains(searchQuery, ignoreCase = true) ||
      item.child.guardianPhone.contains(searchQuery) ||
      item.areaName.contains(searchQuery, ignoreCase = true)

    val matchesGender = selectedGender == "الكل" || item.child.gender == selectedGender
    val matchesDenom = selectedDenom == "الكل" || item.child.denomination == selectedDenom
    val matchesArea = selectedAreaFilter == "all" || item.child.areaId == selectedAreaFilter

    matchesQuery && matchesGender && matchesDenom && matchesArea
  }

  // Delete confirmation dialog
  childToDelete?.let { child ->
    AlertDialog(
      onDismissRequest = { childToDelete = null },
      title = { Text("تأكيد حذف الطفل", fontWeight = FontWeight.Bold) },
      text = { Text("هل أنت متأكد من حذف الطفل: ${child.name}؟ هذا الإجراء لا يمكن التراجع عنه.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteChild(child)
            childToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
        ) {
          Text("نعم، تأكيد الحذف")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { childToDelete = null }) {
          Text("إلغاء")
        }
      }
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.showAddChildDialog.value = true },
        containerColor = BrandBlue,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_add_child")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Add, contentDescription = "إضافة طفل")
          Spacer(modifier = Modifier.width(6.dp))
          Text("إضافة طفل", fontWeight = FontWeight.Bold)
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "إدارة بيانات الأطفال",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Text(
            text = "عرض وتعديل وتصفية سجلات الأطفال المسجلين",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
          )
        }

        Surface(shape = RoundedCornerShape(8.dp), color = BrandSkyLight) {
          Text(
            text = "${filteredChildren.size} طفل",
            style = MaterialTheme.typography.labelMedium.copy(
              fontWeight = FontWeight.Bold,
              color = BrandNavyDark
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Search Box
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("بحث باسم الطفل أو ولي الأمر أو الهاتف...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeutralSlate500) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "مسح", tint = NeutralSlate500)
            }
          }
        },
        modifier = Modifier
          .fillMaxWidth()
          .testTag("children_search_input"),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        // Gender Filters
        listOf("الكل", "بنين", "بنات").forEach { g ->
          FilterChipItem(
            label = g,
            isSelected = selectedGender == g,
            onClick = { selectedGender = g }
          )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Denomination Filters
        listOf("أرثوذكس", "إنجيلي", "كاثوليك").forEach { d ->
          FilterChipItem(
            label = d,
            isSelected = selectedDenom == d,
            onClick = { selectedDenom = if (selectedDenom == d) "الكل" else d }
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Children List
      if (filteredChildren.isEmpty()) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Face, contentDescription = null, tint = NeutralSlate500, modifier = Modifier.size(48.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text("لا توجد سجلات مطابقة للبحث", style = MaterialTheme.typography.bodyLarge.copy(color = NeutralSlate700, fontWeight = FontWeight.Bold))
            Text("جرّب تغيير عبارة البحث أو الفلتر", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
          }
        }
      } else {
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          items(filteredChildren, key = { it.child.id }) { item ->
            val isBoy = item.child.gender == "بنين"
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
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
                .border(1.dp, NeutralSlate200, RoundedCornerShape(14.dp)),
              colors = CardDefaults.cardColors(containerColor = Color.White),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                      modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(if (isBoy) BrandSkyLight else Color(0xFFFCE7F3)),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = if (isBoy) BrandBlue else Color(0xFFDB2777),
                        modifier = Modifier.size(22.dp)
                      )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                      Text(
                        text = item.child.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                          fontWeight = FontWeight.Bold,
                          color = BrandNavyDark
                        )
                      )
                      Text(
                        text = "${item.areaName} • ${item.className}",
                        style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
                      )
                    }
                  }

                  // Gender & Denomination badges
                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = if (isBoy) BrandBlue.copy(alpha = 0.1f) else Color(0xFFFCE7F3)
                    ) {
                      Text(
                        text = item.child.gender,
                        color = if (isBoy) BrandBlue else Color(0xFFDB2777),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }

                    Surface(
                      shape = RoundedCornerShape(6.dp),
                      color = NeutralSlate100
                    ) {
                      Text(
                        text = item.child.denomination,
                        color = BrandNavyDark,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Guardian info & Actions
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = "ولي الأمر: ${item.child.guardianName} (${item.child.guardianPhone})",
                    style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate700, fontSize = 11.sp)
                  )

                  Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(
                      onClick = {
                        viewModel.editingChild.value = item.child
                        viewModel.showAddChildDialog.value = true
                      },
                      modifier = Modifier.size(28.dp)
                    ) {
                      Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = BrandNavyMedium, modifier = Modifier.size(16.dp))
                    }

                    IconButton(
                      onClick = { childToDelete = item.child },
                      modifier = Modifier.size(28.dp)
                    ) {
                      Icon(Icons.Default.Delete, contentDescription = "حذف", tint = AccentCoral, modifier = Modifier.size(16.dp))
                    }
                  }
                }
              }
            }
          }
          item {
            Spacer(modifier = Modifier.height(60.dp))
          }
        }
      }
    }
  }
}

@Composable
private fun FilterChipItem(
  label: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(8.dp),
    color = if (isSelected) BrandNavyDark else NeutralSlate100,
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
  ) {
    Text(
      text = label,
      color = if (isSelected) Color.White else NeutralSlate700,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
      ),
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
    )
  }
}
