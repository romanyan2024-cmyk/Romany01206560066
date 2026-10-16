package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.AccentCoralLight
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.theme.NeutralSlate800

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsBottomSheet(
  item: SearchResultItem?,
  onDismiss: () -> Unit,
  onEditChild: (ChildEntity) -> Unit,
  onDeleteChild: (ChildEntity) -> Unit,
  onEditTeacher: (TeacherEntity) -> Unit,
  onDeleteTeacher: (TeacherEntity) -> Unit,
  onEditClass: (ClassEntity) -> Unit,
  onDeleteClass: (ClassEntity) -> Unit,
  onEditSupervisor: (SupervisorEntity) -> Unit,
  onDeleteSupervisor: (SupervisorEntity) -> Unit,
  sheetState: SheetState,
  modifier: Modifier = Modifier
) {
  if (item == null) return

  val context = LocalContext.current

  ModalBottomSheet(
    onDismissRequest = onDismiss,
    sheetState = sheetState,
    containerColor = Color.White,
    modifier = modifier.testTag("details_bottom_sheet")
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
      // Header
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(BrandSkyLight),
            contentAlignment = Alignment.Center
          ) {
            val icon: ImageVector = when (item.type) {
              SearchResultType.CHILD -> Icons.Default.Face
              SearchResultType.TEACHER -> Icons.Default.Group
              SearchResultType.SUPERVISOR -> Icons.Default.SupervisorAccount
              SearchResultType.CLASS -> Icons.Default.School
              SearchResultType.AREA -> Icons.Default.LocationOn
              else -> Icons.Default.Face
            }
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = BrandNavyMedium,
              modifier = Modifier.size(24.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column {
            Text(
              text = item.title,
              style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = BrandNavyDark,
                fontSize = 20.sp
              )
            )
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = BrandSkyLight
            ) {
              Text(
                text = item.type.label,
                style = MaterialTheme.typography.labelSmall.copy(
                  color = BrandNavyMedium,
                  fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }
        }

        IconButton(onClick = onDismiss) {
          Icon(Icons.Default.Close, contentDescription = "إغلاق", tint = NeutralSlate500)
        }
      }

      Spacer(modifier = Modifier.height(18.dp))

      // Content based on entity type
      item.childRef?.let { child ->
        DetailRow(label = "النوع", value = child.gender)
        DetailRow(label = "الطائفة", value = child.denomination)
        DetailRow(label = "سنة الميلاد", value = "${child.birthYear} (${2026 - child.birthYear} سنوات)")
        DetailRow(label = "ولي الأمر", value = child.guardianName)

        if (child.guardianPhone.isNotBlank()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("هاتف ولي الأمر", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
              Text(child.guardianPhone, style = MaterialTheme.typography.titleMedium.copy(color = BrandNavyDark, fontWeight = FontWeight.Bold))
            }
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${child.guardianPhone}"))
                context.startActivity(intent)
              },
              colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Call, contentDescription = "اتصال", tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("اتصال", color = Color.White)
            }
          }
        }

        DetailRow(label = "الموقع الجغرافي", value = item.locationInfo)
        if (child.address.isNotBlank()) DetailRow(label = "العنوان بالتفصيل", value = child.address)
        if (child.notes.isNotBlank()) DetailRow(label = "ملاحظات وتوجيهات", value = child.notes)

        Spacer(modifier = Modifier.height(20.dp))

        // Actions
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              onEditChild(child)
              onDismiss()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("تعديل البيانات")
          }

          OutlinedButton(
            onClick = {
              onDeleteChild(child)
            },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCoral),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("حذف")
          }
        }
      }

      item.teacherRef?.let { teacher ->
        DetailRow(label = "التخصص والدور", value = teacher.specialty)
        if (teacher.phone.isNotBlank()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("رقم الهاتف", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
              Text(teacher.phone, style = MaterialTheme.typography.titleMedium.copy(color = BrandNavyDark, fontWeight = FontWeight.Bold))
            }
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${teacher.phone}"))
                context.startActivity(intent)
              },
              colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Call, contentDescription = "اتصال", tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("اتصال", color = Color.White)
            }
          }
        }
        DetailRow(label = "المنطقة", value = item.locationInfo)
        if (teacher.notes.isNotBlank()) DetailRow(label = "ملاحظات", value = teacher.notes)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              onEditTeacher(teacher)
              onDismiss()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("تعديل الخادم")
          }

          OutlinedButton(
            onClick = { onDeleteTeacher(teacher) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCoral),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("حذف")
          }
        }
      }

      item.supervisorRef?.let { sup ->
        DetailRow(label = "المحافظة", value = item.locationInfo)
        if (sup.phone.isNotBlank()) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text("رقم التواصل", style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate500))
              Text(sup.phone, style = MaterialTheme.typography.titleMedium.copy(color = BrandNavyDark, fontWeight = FontWeight.Bold))
            }
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${sup.phone}"))
                context.startActivity(intent)
              },
              colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(Icons.Default.Call, contentDescription = "اتصال", tint = Color.White, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("اتصال", color = Color.White)
            }
          }
        }
        if (sup.email.isNotBlank()) DetailRow(label = "البريد الإلكتروني", value = sup.email)
        DetailRow(label = "المناطق المكلف بها", value = item.subtitle)
        if (sup.notes.isNotBlank()) DetailRow(label = "ملاحظات الإشراف", value = sup.notes)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              onEditSupervisor(sup)
              onDismiss()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("تعديل المشرف")
          }

          OutlinedButton(
            onClick = { onDeleteSupervisor(sup) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCoral),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("حذف")
          }
        }
      }

      item.classRef?.let { cls ->
        DetailRow(label = "المرحلة الدراسية", value = cls.stage)
        DetailRow(label = "السعة القصوى", value = "${cls.maxCapacity} طفل")
        DetailRow(label = "المنطقة والمحافظة", value = item.locationInfo)
        DetailRow(label = "تفاصيل الفصل", value = item.subtitle)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Button(
            onClick = {
              onEditClass(cls)
              onDismiss()
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("تعديل الفصل")
          }

          OutlinedButton(
            onClick = { onDeleteClass(cls) },
            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentCoral),
            shape = RoundedCornerShape(12.dp)
          ) {
            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("حذف")
          }
        }
      }

      item.areaRef?.let { area ->
        DetailRow(label = "اسم المنطقة", value = area.name)
        DetailRow(label = "المحافظة التابعة لها", value = item.locationInfo)
        DetailRow(label = "إحصائيات المنطقة", value = item.subtitle)

        Spacer(modifier = Modifier.height(16.dp))
        Button(
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("إغلاق")
        }
      }

      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

@Composable
private fun DetailRow(label: String, value: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp)
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(
        color = NeutralSlate500,
        fontWeight = FontWeight.Medium
      )
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = value,
      style = MaterialTheme.typography.bodyLarge.copy(
        color = NeutralSlate800,
        fontWeight = FontWeight.SemiBold
      )
    )
    Spacer(modifier = Modifier.height(4.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(NeutralSlate100)
    )
  }
}
