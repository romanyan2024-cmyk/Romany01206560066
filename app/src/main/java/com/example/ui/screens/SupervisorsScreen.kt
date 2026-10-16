package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupervisorAccount
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SupervisorEntity
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.AccentEmerald
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
fun SupervisorsScreen(
  viewModel: MainViewModel,
  modifier: Modifier = Modifier
) {
  val supervisors by viewModel.supervisorsWithDetails.collectAsState()
  val context = LocalContext.current

  var searchQuery by remember { mutableStateOf("") }
  var supervisorToDelete by remember { mutableStateOf<SupervisorEntity?>(null) }

  val filteredSupervisors = supervisors.filter { item ->
    searchQuery.isBlank() ||
      item.supervisor.name.contains(searchQuery, ignoreCase = true) ||
      item.governorateName.contains(searchQuery, ignoreCase = true) ||
      item.assignedAreasNames.any { it.contains(searchQuery, ignoreCase = true) } ||
      item.supervisor.phone.contains(searchQuery)
  }

  supervisorToDelete?.let { sup ->
    AlertDialog(
      onDismissRequest = { supervisorToDelete = null },
      title = { Text("تأكيد حذف المشرف", fontWeight = FontWeight.Bold) },
      text = { Text("هل أنت متأكد من حذف ${sup.name} وإلغاء كافة تكليفات الإشراف المرتبطة به؟") },
      confirmButton = {
        Button(
          onClick = {
            viewModel.deleteSupervisor(sup)
            supervisorToDelete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
        ) {
          Text("تأكيد الحذف")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { supervisorToDelete = null }) {
          Text("إلغاء")
        }
      }
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.showAddSupervisorDialog.value = true },
        containerColor = AccentPurple,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_add_supervisor")
      ) {
        Row(modifier = Modifier.padding(horizontal = 14.dp), verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Add, contentDescription = "إضافة مشرف")
          Spacer(modifier = Modifier.width(6.dp))
          Text("إضافة مشرف", fontWeight = FontWeight.Bold)
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
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "المشرفون والتكليفات",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Black,
              color = BrandNavyDark
            )
          )
          Text(
            text = "إشراف القطاعات والمحافظات والمناطق",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500)
          )
        }
        Surface(shape = RoundedCornerShape(8.dp), color = BrandSkyLight) {
          Text(
            text = "${filteredSupervisors.size} مشرف",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        label = { Text("بحث باسم المشرف أو المحافظة أو المنطقة المكلف بها...") },
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
        items(filteredSupervisors, key = { it.supervisor.id }) { item ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(14.dp))
              .clickable {
                viewModel.showDetail(
                  SearchResultItem(
                    id = item.supervisor.id,
                    type = SearchResultType.SUPERVISOR,
                    title = item.supervisor.name,
                    subtitle = "المناطق: " + item.assignedAreasNames.joinToString("، "),
                    locationInfo = item.governorateName,
                    tag = "مشرف",
                    supervisorRef = item.supervisor
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
                  Surface(shape = CircleShape, color = AccentPurple.copy(alpha = 0.15f), modifier = Modifier.size(40.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                      Icon(Icons.Default.SupervisorAccount, contentDescription = null, tint = AccentPurple, modifier = Modifier.size(22.dp))
                    }
                  }
                  Spacer(modifier = Modifier.width(10.dp))
                  Column {
                    Text(item.supervisor.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark))
                    Text(item.governorateName, style = MaterialTheme.typography.bodySmall.copy(color = BrandBlue, fontWeight = FontWeight.Medium))
                  }
                }

                Surface(shape = RoundedCornerShape(6.dp), color = NeutralSlate100) {
                  Text(
                    text = "${item.assignedAreasNames.size} مناطق",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(10.dp))

              if (item.assignedAreasNames.isNotEmpty()) {
                Text(
                  text = "المناطق المكلف بها: " + item.assignedAreasNames.joinToString(" • "),
                  style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate700, fontWeight = FontWeight.SemiBold)
                )
              }

              if (item.supervisor.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = item.supervisor.notes,
                  style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500, fontSize = 11.sp)
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                if (item.supervisor.phone.isNotBlank()) {
                  Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = AccentEmerald.copy(alpha = 0.12f),
                    modifier = Modifier
                      .clip(RoundedCornerShape(8.dp))
                      .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${item.supervisor.phone}"))
                        context.startActivity(intent)
                      }
                  ) {
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                      Icon(Icons.Default.Call, contentDescription = null, tint = AccentEmerald, modifier = Modifier.size(14.dp))
                      Spacer(modifier = Modifier.width(4.dp))
                      Text(item.supervisor.phone, color = AccentEmerald, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }
                  }
                } else {
                  Spacer(modifier = Modifier.width(1.dp))
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                  IconButton(
                    onClick = {
                      viewModel.editingSupervisor.value = item.supervisor
                      viewModel.showAddSupervisorDialog.value = true
                    },
                    modifier = Modifier.size(28.dp)
                  ) {
                    Icon(Icons.Default.Edit, contentDescription = "تعديل", tint = BrandNavyMedium, modifier = Modifier.size(16.dp))
                  }

                  IconButton(
                    onClick = { supervisorToDelete = item.supervisor },
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
