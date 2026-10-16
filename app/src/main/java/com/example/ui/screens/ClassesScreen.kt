package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.example.data.model.ClassEntity
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.AccentEmerald
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
fun ClassesScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val classes by viewModel.classesWithDetails.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  var classToDelete by remember { mutableStateOf<ClassEntity?>(null) }

  val filteredClasses = classes.filter { item ->
    searchQuery.isBlank() ||
      item.classEntity.name.contains(searchQuery, ignoreCase = true) ||
      item.areaName.contains(searchQuery, ignoreCase = true) ||
      item.teacherName.contains(searchQuery, ignoreCase = true) ||
      item.classEntity.stage.contains(searchQuery, ignoreCase = true)
  }

  classToDelete?.let { cls ->
    AlertDialog(
      onDismissRequest = { classToDelete = null },
      title = { Text("تأكيد حذف الفصل", fontWeight = FontWeight.Bold) },
      text = { Text("هل أنت متأكد من حذف فصل: ${cls.name}؟ لن يتم الحذف إذا كان هناك أطفال مسجلين به.") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteClass(cls)
            classToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
        ) {
          Text("تأكيد الحذف")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { classToDelete = null }) {
          Text("إلغاء")
        }
      }
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.showAddClassDialog.value = true },
        containerColor = AccentEmerald,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_add_class")
      ) {
        Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Add, contentDescription = "إضافة فصل")
          Spacer(modifier = Modifier.width(6.dp))
          Text("إضافة فصل", fontWeight = FontWeight.Bold)
        }
      }
    }
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
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
            text = "إدارة الفصول والمراحل",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Text(
            text = "الفصول التابعة لبرنامج صناع القادة 2026",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
          )
        }
        Surface(shape = RoundedCornerShape(8.dp), color = BrandSkyLight) {
          Text(
            text = "${filteredClasses.size} فصل",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Search Box
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("بحث باسم الفصل أو المرحلة أو المنطقة...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeutralSlate500) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = null, tint = NeutralSlate500)
            }
          }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp)
      )

      Spacer(modifier = Modifier.height(14.dp))

      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(filteredClasses, key = { it.classEntity.id }) { item ->
          val capacity = maxOf(1, item.classEntity.maxCapacity)
          val progress = item.childrenCount.toFloat() / capacity.toFloat()

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                viewModel.showDetail(
                  SearchResultItem(
                    id = item.classEntity.id,
                    type = SearchResultType.CLASS,
                    title = item.classEntity.name,
                    subtitle = "المدرس: ${item.teacherName} • ${item.childrenCount} طفل",
                    locationInfo = "${item.governorateName} - ${item.areaName}",
                    tag = item.classEntity.stage,
                    classRef = item.classEntity
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
                  Surface(shape = CircleShape, color = BrandSkyLight, modifier = Modifier.size(38.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(Icons.Default.School, contentDescription = null, tint = BrandNavyMedium, modifier = Modifier.size(20.dp))
                    }
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(item.classEntity.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark))
                    Text("${item.areaName} • ${item.governorateName}", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
                  }
                }

                Surface(shape = RoundedCornerShape(6.dp), color = NeutralSlate100) {
                  Text(
                    text = item.classEntity.stage,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandNavyMedium),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = "المدرس المسؤول: ${item.teacherName}",
                style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate700)
              )

              Spacer(modifier = Modifier.height(8.dp))

              // Capacity Bar
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "تسجيل الأطفال: ${item.childrenCount} من أصل ${item.classEntity.maxCapacity}",
                  style = MaterialTheme.typography.labelSmall.copy(color = NeutralSlate500)
                )
                Text(
                  text = "${(progress * 100).toInt()}%",
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandBlue)
                )
              }

              Spacer(modifier = Modifier.height(4.dp))
              LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                  .fillMaxWidth()
                  .height(6.dp)
                  .clip(RoundedCornerShape(3.dp)),
                color = if (progress >= 1f) AccentCoral else BrandBlue,
                trackColor = NeutralSlate100
              )

              Spacer(modifier = Modifier.height(10.dp))

              // Actions
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
              ) {
                IconButton(
                  onClick = {
                    viewModel.editingClass.value = item.classEntity
                    viewModel.showAddClassDialog.value = true
                  },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = BrandNavyMedium, modifier = Modifier.size(16.dp))
                }

                IconButton(
                  onClick = { classToDelete = item.classEntity },
                  modifier = Modifier.size(28.dp)
                ) {
                  Icon(Icons.Default.Delete, contentDescription = "حذف", tint = AccentCoral, modifier = Modifier.size(16.dp))
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
