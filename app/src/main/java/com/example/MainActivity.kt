package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.data.db.AppDatabase
import com.example.data.repository.AppRepository
import com.example.ui.components.AppNavigation
import com.example.ui.components.AppTopBar
import com.example.ui.components.DetailsBottomSheet
import com.example.ui.components.AddEditChildDialog
import com.example.ui.components.AddEditClassDialog
import com.example.ui.components.AddEditSupervisorDialog
import com.example.ui.components.AddEditTeacherDialog
import com.example.ui.model.AppTab
import com.example.ui.model.UserRole
import com.example.ui.screens.AreasBrowserScreen
import com.example.ui.screens.ChildrenScreen
import com.example.ui.screens.ClassesScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.GlobalSearchScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.SupervisorsScreen
import com.example.ui.screens.TeachersScreen
import com.example.ui.theme.AccentCoral
import com.example.ui.theme.AccentEmerald
import com.example.ui.theme.AccentGold
import com.example.ui.theme.BrandBlue
import com.example.ui.theme.BrandNavyDark
import com.example.ui.theme.BrandNavyMedium
import com.example.ui.theme.BrandSkyLight
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeutralSlate100
import com.example.ui.theme.NeutralSlate50
import com.example.ui.theme.NeutralSlate500
import com.example.ui.theme.NeutralSlate700
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    val database = AppDatabase.getDatabase(applicationContext, lifecycleScope)
    val repository = AppRepository(database.appDao())

    setContent {
      val viewModel = remember { MainViewModel(repository) }

      MyApplicationTheme {
        // Enforce Arabic RTL layout globally
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
          Suna3AlQadaApp(viewModel = viewModel)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Suna3AlQadaApp(viewModel: MainViewModel) {
  val session by viewModel.userSession.collectAsState()
  val currentTab by viewModel.currentTab.collectAsState()
  val statusMessage by viewModel.statusMessage.collectAsState()
  val selectedDetailItem by viewModel.selectedDetailItem.collectAsState()

  // Dialog triggers
  val showAddChild by viewModel.showAddChildDialog.collectAsState()
  val showAddTeacher by viewModel.showAddTeacherDialog.collectAsState()
  val showAddClass by viewModel.showAddClassDialog.collectAsState()
  val showAddSupervisor by viewModel.showAddSupervisorDialog.collectAsState()

  // Edit triggers
  val editingChild by viewModel.editingChild.collectAsState()
  val editingTeacher by viewModel.editingTeacher.collectAsState()
  val editingClass by viewModel.editingClass.collectAsState()
  val editingSupervisor by viewModel.editingSupervisor.collectAsState()

  // Database resources for dialog pickers
  val areas by viewModel.areas.collectAsState()
  val classes by viewModel.classes.collectAsState()
  val teachers by viewModel.teachers.collectAsState()
  val governorates by viewModel.governorates.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }
  var showProfileDialog by remember { mutableStateOf(false) }
  var showQuickAddChoiceDialog by remember { mutableStateOf(false) }

  // Handle status messages
  LaunchedEffect(statusMessage) {
    statusMessage?.let { msg ->
      snackbarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
      viewModel.clearStatus()
    }
  }

  if (!session.isLoggedIn) {
    LoginScreen(viewModel = viewModel)
    return
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = NeutralSlate50,
    topBar = {
      AppTopBar(
        session = session,
        onOpenSearch = { viewModel.setTab(AppTab.SEARCH) },
        onQuickAdd = { showQuickAddChoiceDialog = true },
        onLogout = { viewModel.logout() },
        onProfileClick = { showProfileDialog = true }
      )
    },
    bottomBar = {
      AppNavigation(
        currentTab = currentTab,
        onTabSelected = { viewModel.setTab(it) }
      )
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      when (currentTab) {
        AppTab.DASHBOARD -> DashboardScreen(viewModel = viewModel)
        AppTab.AREAS -> AreasBrowserScreen(viewModel = viewModel)
        AppTab.CHILDREN -> ChildrenScreen(viewModel = viewModel)
        AppTab.CLASSES -> ClassesScreen(viewModel = viewModel)
        AppTab.TEACHERS -> TeachersScreen(viewModel = viewModel)
        AppTab.SUPERVISORS -> SupervisorsScreen(viewModel = viewModel)
        AppTab.SEARCH -> GlobalSearchScreen(viewModel = viewModel)
        AppTab.REPORTS -> ReportsScreen(viewModel = viewModel)
      }
    }
  }

  // Details Bottom Sheet
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
  if (selectedDetailItem != null) {
    DetailsBottomSheet(
      item = selectedDetailItem,
      onDismiss = { viewModel.dismissDetail() },
      onEditChild = {
        viewModel.editingChild.value = it
        viewModel.showAddChildDialog.value = true
      },
      onDeleteChild = { viewModel.deleteChild(it) },
      onEditTeacher = {
        viewModel.editingTeacher.value = it
        viewModel.showAddTeacherDialog.value = true
      },
      onDeleteTeacher = { viewModel.deleteTeacher(it) },
      onEditClass = {
        viewModel.editingClass.value = it
        viewModel.showAddClassDialog.value = true
      },
      onDeleteClass = { viewModel.deleteClass(it) },
      onEditSupervisor = {
        viewModel.editingSupervisor.value = it
        viewModel.showAddSupervisorDialog.value = true
      },
      onDeleteSupervisor = { viewModel.deleteSupervisor(it) },
      sheetState = sheetState
    )
  }

  // Quick Add Choice Dialog
  if (showQuickAddChoiceDialog) {
    AlertDialog(
      onDismissRequest = { showQuickAddChoiceDialog = false },
      title = { Text("اختر ما تريد إضافته للنظام", fontWeight = FontWeight.Black) },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Button(
            onClick = {
              showQuickAddChoiceDialog = false
              viewModel.showAddChildDialog.value = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBlue)
          ) {
            Text("إضافة طفل جديد (+)")
          }
          Button(
            onClick = {
              showQuickAddChoiceDialog = false
              viewModel.showAddTeacherDialog.value = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = BrandNavyDark)
          ) {
            Text("إضافة خادم / مدرس (+)")
          }
          Button(
            onClick = {
              showQuickAddChoiceDialog = false
              viewModel.showAddClassDialog.value = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentEmerald)
          ) {
            Text("إضافة فصل جديد (+)")
          }
          Button(
            onClick = {
              showQuickAddChoiceDialog = false
              viewModel.showAddSupervisorDialog.value = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
          ) {
            Text("إضافة مشرف جديد (+)")
          }
        }
      },
      confirmButton = {},
      dismissButton = {
        OutlinedButton(onClick = { showQuickAddChoiceDialog = false }) {
          Text("إلغاء")
        }
      }
    )
  }

  // User Profile & Session Dialog
  if (showProfileDialog) {
    AlertDialog(
      onDismissRequest = { showProfileDialog = false },
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Person, contentDescription = null, tint = BrandBlue)
          Spacer(modifier = Modifier.width(8.dp))
          Text("الملف الشخصي والجلسة", fontWeight = FontWeight.Black)
        }
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = session.fullName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark)
          )
          Surface(shape = RoundedCornerShape(8.dp), color = BrandSkyLight) {
            Text(
              text = "الصلاحية: ${session.role.titleArabic}",
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandNavyDark),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
          Text(
            text = "اسم المستخدم: ${session.username}",
            style = MaterialTheme.typography.bodySmall.copy(color = NeutralSlate700)
          )
          Text(
            text = "حالة الاتصال: متصل بقاعدة بيانات صُنّاع القادة 2026 المحلية النشطة",
            style = MaterialTheme.typography.bodySmall.copy(color = AccentEmerald, fontWeight = FontWeight.Bold)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showProfileDialog = false
            viewModel.logout()
          },
          colors = ButtonDefaults.buttonColors(containerColor = AccentCoral)
        ) {
          Icon(Icons.Default.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("تسجيل الخروج")
        }
      },
      dismissButton = {
        OutlinedButton(onClick = { showProfileDialog = false }) {
          Text("إغلاق")
        }
      }
    )
  }

  // Add / Edit Child Dialog
  if (showAddChild || editingChild != null) {
    AddEditChildDialog(
      initialChild = editingChild,
      areas = areas,
      classes = classes,
      onDismiss = {
        viewModel.showAddChildDialog.value = false
        viewModel.editingChild.value = null
      },
      onSave = { id, name, gender, denomination, birthYear, guardianName, guardianPhone, areaId, classId, address, notes ->
        viewModel.saveChild(
          id = id,
          name = name,
          gender = gender,
          denomination = denomination,
          birthYear = birthYear,
          guardianName = guardianName,
          guardianPhone = guardianPhone,
          areaId = areaId,
          classId = classId,
          address = address,
          notes = notes
        )
      }
    )
  }

  // Add / Edit Teacher Dialog
  if (showAddTeacher || editingTeacher != null) {
    AddEditTeacherDialog(
      initialTeacher = editingTeacher,
      areas = areas,
      onDismiss = {
        viewModel.showAddTeacherDialog.value = false
        viewModel.editingTeacher.value = null
      },
      onSave = { id, name, phone, areaId, specialty, notes ->
        viewModel.saveTeacher(
          id = id,
          name = name,
          phone = phone,
          areaId = areaId,
          specialty = specialty,
          notes = notes
        )
      }
    )
  }

  // Add / Edit Class Dialog
  if (showAddClass || editingClass != null) {
    AddEditClassDialog(
      initialClass = editingClass,
      areas = areas,
      teachers = teachers,
      onDismiss = {
        viewModel.showAddClassDialog.value = false
        viewModel.editingClass.value = null
      },
      onSave = { id, name, areaId, stage, teacherId, maxCapacity ->
        viewModel.saveClass(
          id = id,
          name = name,
          areaId = areaId,
          stage = stage,
          teacherId = teacherId,
          maxCapacity = maxCapacity
        )
      }
    )
  }

  // Add / Edit Supervisor Dialog
  if (showAddSupervisor || editingSupervisor != null) {
    AddEditSupervisorDialog(
      initialSupervisor = editingSupervisor,
      governorates = governorates,
      areas = areas,
      onDismiss = {
        viewModel.showAddSupervisorDialog.value = false
        viewModel.editingSupervisor.value = null
      },
      onSave = { id, name, phone, email, governorateId, notes, assignedAreas ->
        viewModel.saveSupervisor(
          id = id,
          name = name,
          phone = phone,
          email = email,
          governorateId = governorateId,
          notes = notes,
          assignedAreaIds = assignedAreas
        )
      }
    )
  }
}
