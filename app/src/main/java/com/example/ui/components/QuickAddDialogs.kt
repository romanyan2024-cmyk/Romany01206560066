package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.GovernorateEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditChildDialog(
  initialChild: ChildEntity? = null,
  areas: List<AreaEntity>,
  classes: List<ClassEntity>,
  onDismiss: () -> Unit,
  onSave: (
    id: String?,
    name: String,
    gender: String,
    denomination: String,
    birthYear: Int,
    guardianName: String,
    guardianPhone: String,
    areaId: String,
    classId: String?,
    address: String,
    notes: String
  ) -> Unit
) {
  var name by remember { mutableStateOf(initialChild?.name ?: "") }
  var gender by remember { mutableStateOf(initialChild?.gender ?: "بنين") }
  var denomination by remember { mutableStateOf(initialChild?.denomination ?: "أرثوذكس") }
  var birthYearStr by remember { mutableStateOf(initialChild?.birthYear?.toString() ?: "2016") }
  var guardianName by remember { mutableStateOf(initialChild?.guardianName ?: "") }
  var guardianPhone by remember { mutableStateOf(initialChild?.guardianPhone ?: "") }
  var selectedAreaId by remember { mutableStateOf(initialChild?.areaId ?: areas.firstOrNull()?.id ?: "") }
  var selectedClassId by remember { mutableStateOf<String?>(initialChild?.classId) }
  var address by remember { mutableStateOf(initialChild?.address ?: "") }
  var notes by remember { mutableStateOf(initialChild?.notes ?: "") }

  var areaMenuExpanded by remember { mutableStateOf(false) }
  var classMenuExpanded by remember { mutableStateOf(false) }

  val filteredClasses = classes.filter { it.areaId == selectedAreaId }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (initialChild == null) "إضافة طفل جديد" else "تعديل بيانات الطفل",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Black,
          color = BrandNavyDark
        )
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Name
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("اسم الطفل الرباعي *") },
          modifier = Modifier.fillMaxWidth().testTag("child_name_input"),
          singleLine = true
        )

        // Gender Selector
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("بنين", "بنات").forEach { g ->
            val isSelected = gender == g
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) BrandBlue else NeutralSlate100,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable { gender = g }
            ) {
              Text(
                text = g,
                color = if (isSelected) Color.White else NeutralSlate700,
                modifier = Modifier.padding(vertical = 10.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        // Denomination Selector
        Text("الطائفة:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf("أرثوذكس", "إنجيلي", "كاثوليك").forEach { denom ->
            val isSelected = denomination == denom
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) BrandNavyMedium else NeutralSlate100,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable { denomination = denom }
            ) {
              Text(
                text = denom,
                color = if (isSelected) Color.White else NeutralSlate700,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }

        // Birth Year
        OutlinedTextField(
          value = birthYearStr,
          onValueChange = { birthYearStr = it },
          label = { Text("سنة الميلاد (مثال: 2016)") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        // Area Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
          val selectedArea = areas.find { it.id == selectedAreaId }
          OutlinedTextField(
            value = selectedArea?.name ?: "اختر المنطقة",
            onValueChange = {},
            readOnly = true,
            label = { Text("المنطقة *") },
            trailingIcon = {
              Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.clickable { areaMenuExpanded = true }
              )
            },
            modifier = Modifier.fillMaxWidth().clickable { areaMenuExpanded = true }
          )
          DropdownMenu(
            expanded = areaMenuExpanded,
            onDismissRequest = { areaMenuExpanded = false }
          ) {
            areas.forEach { area ->
              DropdownMenuItem(
                text = { Text(area.name) },
                onClick = {
                  selectedAreaId = area.id
                  selectedClassId = null
                  areaMenuExpanded = false
                }
              )
            }
          }
        }

        // Class Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
          val selectedClass = classes.find { it.id == selectedClassId }
          OutlinedTextField(
            value = selectedClass?.name ?: if (filteredClasses.isEmpty()) "لا توجد فصول في هذه المنطقة" else "اختر الفصل",
            onValueChange = {},
            readOnly = true,
            label = { Text("الفصل الدراسي") },
            trailingIcon = {
              Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.clickable { classMenuExpanded = true }
              )
            },
            modifier = Modifier.fillMaxWidth().clickable { classMenuExpanded = true }
          )
          DropdownMenu(
            expanded = classMenuExpanded,
            onDismissRequest = { classMenuExpanded = false }
          ) {
            filteredClasses.forEach { cls ->
              DropdownMenuItem(
                text = { Text("${cls.name} (${cls.stage})") },
                onClick = {
                  selectedClassId = cls.id
                  classMenuExpanded = false
                }
              )
            }
          }
        }

        // Guardian
        OutlinedTextField(
          value = guardianName,
          onValueChange = { guardianName = it },
          label = { Text("اسم ولي الأمر") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = guardianPhone,
          onValueChange = { guardianPhone = it },
          label = { Text("هاتف التواصل") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        // Address & Notes
        OutlinedTextField(
          value = address,
          onValueChange = { address = it },
          label = { Text("العنوان بالتفصيل") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("ملاحظات") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val yr = birthYearStr.toIntOrNull() ?: 2016
          onSave(
            initialChild?.id,
            name,
            gender,
            denomination,
            yr,
            guardianName,
            guardianPhone,
            selectedAreaId,
            selectedClassId,
            address,
            notes
          )
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
        modifier = Modifier.testTag("save_child_button")
      ) {
        Text("حفظ البيانات")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("إلغاء")
      }
    }
  )
}

@Composable
fun AddEditTeacherDialog(
  initialTeacher: TeacherEntity? = null,
  areas: List<AreaEntity>,
  onDismiss: () -> Unit,
  onSave: (id: String?, name: String, phone: String, areaId: String, specialty: String, notes: String) -> Unit
) {
  var name by remember { mutableStateOf(initialTeacher?.name ?: "") }
  var phone by remember { mutableStateOf(initialTeacher?.phone ?: "") }
  var selectedAreaId by remember { mutableStateOf(initialTeacher?.areaId ?: areas.firstOrNull()?.id ?: "") }
  var specialty by remember { mutableStateOf(initialTeacher?.specialty ?: "خادم مرحلة ابتدائي وبناء مهارات") }
  var notes by remember { mutableStateOf(initialTeacher?.notes ?: "") }
  var areaMenuExpanded by remember { mutableStateOf(false) }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (initialTeacher == null) "إضافة خادم / مدرس" else "تعديل بيانات الخادم",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark)
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("الاسم بالكامل *") },
          modifier = Modifier.fillMaxWidth().testTag("teacher_name_input"),
          singleLine = true
        )

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("رقم الهاتف") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        Box(modifier = Modifier.fillMaxWidth()) {
          val selectedArea = areas.find { it.id == selectedAreaId }
          OutlinedTextField(
            value = selectedArea?.name ?: "اختر المنطقة",
            onValueChange = {},
            readOnly = true,
            label = { Text("المنطقة المكلف بالخدمة فيها *") },
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.clickable { areaMenuExpanded = true })
            },
            modifier = Modifier.fillMaxWidth().clickable { areaMenuExpanded = true }
          )
          DropdownMenu(
            expanded = areaMenuExpanded,
            onDismissRequest = { areaMenuExpanded = false }
          ) {
            areas.forEach { area ->
              DropdownMenuItem(
                text = { Text(area.name) },
                onClick = {
                  selectedAreaId = area.id
                  areaMenuExpanded = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = specialty,
          onValueChange = { specialty = it },
          label = { Text("التخصص والدور التدريبي") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("ملاحظات / المؤهل") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onSave(initialTeacher?.id, name, phone, selectedAreaId, specialty, notes) },
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
        modifier = Modifier.testTag("save_teacher_button")
      ) {
        Text("حفظ البيانات")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("إلغاء")
      }
    }
  )
}

@Composable
fun AddEditClassDialog(
  initialClass: ClassEntity? = null,
  areas: List<AreaEntity>,
  teachers: List<TeacherEntity>,
  onDismiss: () -> Unit,
  onSave: (id: String?, name: String, areaId: String, stage: String, teacherId: String?, maxCapacity: Int) -> Unit
) {
  var name by remember { mutableStateOf(initialClass?.name ?: "") }
  var selectedAreaId by remember { mutableStateOf(initialClass?.areaId ?: areas.firstOrNull()?.id ?: "") }
  var stage by remember { mutableStateOf(initialClass?.stage ?: "ابتدائي متقدم") }
  var selectedTeacherId by remember { mutableStateOf<String?>(initialClass?.teacherId) }
  var capacityStr by remember { mutableStateOf(initialClass?.maxCapacity?.toString() ?: "25") }

  var areaMenuExpanded by remember { mutableStateOf(false) }
  var teacherMenuExpanded by remember { mutableStateOf(false) }

  val filteredTeachers = teachers.filter { it.areaId == selectedAreaId }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (initialClass == null) "إضافة فصل جديد" else "تعديل الفصل",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark)
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("اسم الفصل * (مثال: فصل فرسان النور)") },
          modifier = Modifier.fillMaxWidth().testTag("class_name_input"),
          singleLine = true
        )

        Box(modifier = Modifier.fillMaxWidth()) {
          val selectedArea = areas.find { it.id == selectedAreaId }
          OutlinedTextField(
            value = selectedArea?.name ?: "اختر المنطقة",
            onValueChange = {},
            readOnly = true,
            label = { Text("المنطقة التابع لها الفصل *") },
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.clickable { areaMenuExpanded = true })
            },
            modifier = Modifier.fillMaxWidth().clickable { areaMenuExpanded = true }
          )
          DropdownMenu(expanded = areaMenuExpanded, onDismissRequest = { areaMenuExpanded = false }) {
            areas.forEach { area ->
              DropdownMenuItem(
                text = { Text(area.name) },
                onClick = {
                  selectedAreaId = area.id
                  selectedTeacherId = null
                  areaMenuExpanded = false
                }
              )
            }
          }
        }

        // Stage
        OutlinedTextField(
          value = stage,
          onValueChange = { stage = it },
          label = { Text("المرحلة الدراسية (ابتدائي مبكر / متقدم / إعدادي)") },
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        // Teacher
        Box(modifier = Modifier.fillMaxWidth()) {
          val selectedTeacher = teachers.find { it.id == selectedTeacherId }
          OutlinedTextField(
            value = selectedTeacher?.name ?: if (filteredTeachers.isEmpty()) "لا يوجد خدام بالمنطقة حاليًا" else "اختر الخادم المسؤول",
            onValueChange = {},
            readOnly = true,
            label = { Text("الخادم / المدرس المسؤول") },
            trailingIcon = {
              Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.clickable { teacherMenuExpanded = true })
            },
            modifier = Modifier.fillMaxWidth().clickable { teacherMenuExpanded = true }
          )
          DropdownMenu(expanded = teacherMenuExpanded, onDismissRequest = { teacherMenuExpanded = false }) {
            filteredTeachers.forEach { tch ->
              DropdownMenuItem(
                text = { Text(tch.name) },
                onClick = {
                  selectedTeacherId = tch.id
                  teacherMenuExpanded = false
                }
              )
            }
          }
        }

        OutlinedTextField(
          value = capacityStr,
          onValueChange = { capacityStr = it },
          label = { Text("السعة القصوى للأطفال") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )
      }
    },
    confirmButton = {
      Button(
        onClick = {
          val cap = capacityStr.toIntOrNull() ?: 25
          onSave(initialClass?.id, name, selectedAreaId, stage, selectedTeacherId, cap)
        },
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
        modifier = Modifier.testTag("save_class_button")
      ) {
        Text("حفظ البيانات")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("إلغاء")
      }
    }
  )
}

@Composable
fun AddEditSupervisorDialog(
  initialSupervisor: SupervisorEntity? = null,
  governorates: List<GovernorateEntity>,
  areas: List<AreaEntity>,
  onDismiss: () -> Unit,
  onSave: (id: String?, name: String, phone: String, email: String, governorateId: String, notes: String, assignedAreas: List<String>) -> Unit
) {
  var name by remember { mutableStateOf(initialSupervisor?.name ?: "") }
  var phone by remember { mutableStateOf(initialSupervisor?.phone ?: "") }
  var email by remember { mutableStateOf(initialSupervisor?.email ?: "") }
  var selectedGovId by remember { mutableStateOf(initialSupervisor?.governorateId ?: "minya") }
  var notes by remember { mutableStateOf(initialSupervisor?.notes ?: "") }
  var selectedAreaIds by remember { mutableStateOf<Set<String>>(emptySet()) }

  val govAreas = areas.filter { it.governorateId == selectedGovId }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (initialSupervisor == null) "إضافة مشرف جديد" else "تعديل بيانات المشرف",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandNavyDark)
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("اسم المشرف بالكامل *") },
          modifier = Modifier.fillMaxWidth().testTag("supervisor_name_input"),
          singleLine = true
        )

        OutlinedTextField(
          value = phone,
          onValueChange = { phone = it },
          label = { Text("هاتف التواصل") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        OutlinedTextField(
          value = email,
          onValueChange = { email = it },
          label = { Text("البريد الإلكتروني") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
          modifier = Modifier.fillMaxWidth(),
          singleLine = true
        )

        // Governorate Toggle
        Text("المحافظة:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          governorates.forEach { gov ->
            val isSelected = selectedGovId == gov.id
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = if (isSelected) BrandNavyMedium else NeutralSlate100,
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                  selectedGovId = gov.id
                  selectedAreaIds = emptySet()
                }
            ) {
              Text(
                text = gov.name,
                color = if (isSelected) Color.White else NeutralSlate700,
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
              )
            }
          }
        }

        // Multi-area selector
        Text("تكليف المناطق التابعة له:", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          govAreas.forEach { area ->
            val isChecked = area.id in selectedAreaIds
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .clickable {
                  selectedAreaIds = if (isChecked) selectedAreaIds - area.id else selectedAreaIds + area.id
                }
                .padding(vertical = 4.dp, horizontal = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .width(20.dp)
                  .height(20.dp)
                  .clip(RoundedCornerShape(4.dp))
                  .background(if (isChecked) BrandBlue else NeutralSlate100),
                contentAlignment = Alignment.Center
              ) {
                if (isChecked) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                }
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(area.name, style = MaterialTheme.typography.bodyMedium)
            }
          }
        }

        OutlinedTextField(
          value = notes,
          onValueChange = { notes = it },
          label = { Text("ملاحظات التكليف والإشراف") },
          modifier = Modifier.fillMaxWidth(),
          maxLines = 2
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onSave(initialSupervisor?.id, name, phone, email, selectedGovId, notes, selectedAreaIds.toList()) },
        colors = ButtonDefaults.buttonColors(containerColor = BrandBlue),
        modifier = Modifier.testTag("save_supervisor_button")
      ) {
        Text("حفظ البيانات")
      }
    },
    dismissButton = {
      OutlinedButton(onClick = onDismiss) {
        Text("إلغاء")
      }
    }
  )
}
