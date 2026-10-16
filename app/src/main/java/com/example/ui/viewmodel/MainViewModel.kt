package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.GovernorateEntity
import com.example.data.model.SupervisorAreaAssignmentEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity
import com.example.data.repository.AppRepository
import com.example.ui.model.AppTab
import com.example.ui.model.AreaWithDetails
import com.example.ui.model.ChildWithDetails
import com.example.ui.model.ClassWithDetails
import com.example.ui.model.DashboardStats
import com.example.ui.model.SearchResultItem
import com.example.ui.model.SearchResultType
import com.example.ui.model.SupervisorWithDetails
import com.example.ui.model.TeacherWithDetails
import com.example.ui.model.UserRole
import com.example.ui.model.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)

class MainViewModel(private val repository: AppRepository) : ViewModel() {

  private val _currentTab = MutableStateFlow(AppTab.DASHBOARD)
  val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

  private val _userSession = MutableStateFlow(UserSession())
  val userSession: StateFlow<UserSession> = _userSession.asStateFlow()

  // Selected Area filter in browser
  private val _selectedAreaId = MutableStateFlow<String?>(null)
  val selectedAreaId: StateFlow<String?> = _selectedAreaId.asStateFlow()

  private val _selectedGovernorateFilter = MutableStateFlow("all")
  val selectedGovernorateFilter: StateFlow<String> = _selectedGovernorateFilter.asStateFlow()

  // Search State
  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _searchFilterType = MutableStateFlow(SearchResultType.ALL)
  val searchFilterType: StateFlow<SearchResultType> = _searchFilterType.asStateFlow()

  // Detail Sheet
  private val _selectedDetailItem = MutableStateFlow<SearchResultItem?>(null)
  val selectedDetailItem: StateFlow<SearchResultItem?> = _selectedDetailItem.asStateFlow()

  // Dialog triggers
  val showAddChildDialog = MutableStateFlow(false)
  val showAddTeacherDialog = MutableStateFlow(false)
  val showAddClassDialog = MutableStateFlow(false)
  val showAddSupervisorDialog = MutableStateFlow(false)

  // Edit states
  val editingChild = MutableStateFlow<ChildEntity?>(null)
  val editingTeacher = MutableStateFlow<TeacherEntity?>(null)
  val editingClass = MutableStateFlow<ClassEntity?>(null)
  val editingSupervisor = MutableStateFlow<SupervisorEntity?>(null)

  // Feedback message (Snackbar/toast banner)
  private val _statusMessage = MutableStateFlow<String?>(null)
  val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

  init {
    viewModelScope.launch {
      repository.ensureDataSeeded()
    }
  }

  fun setTab(tab: AppTab) {
    _currentTab.value = tab
  }

  fun setSelectedAreaId(areaId: String?) {
    _selectedAreaId.value = areaId
  }

  fun setGovernorateFilter(filter: String) {
    _selectedGovernorateFilter.value = filter
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSearchFilterType(type: SearchResultType) {
    _searchFilterType.value = type
  }

  fun showDetail(item: SearchResultItem) {
    _selectedDetailItem.value = item
  }

  fun dismissDetail() {
    _selectedDetailItem.value = null
  }

  fun showStatus(message: String) {
    _statusMessage.value = message
  }

  fun clearStatus() {
    _statusMessage.value = null
  }

  // Raw Database Flows
  val governorates: StateFlow<List<GovernorateEntity>> = repository.governorates.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val areas: StateFlow<List<AreaEntity>> = repository.areas.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val supervisors: StateFlow<List<SupervisorEntity>> = repository.supervisors.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val assignments: StateFlow<List<SupervisorAreaAssignmentEntity>> = repository.assignments.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val teachers: StateFlow<List<TeacherEntity>> = repository.teachers.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val classes: StateFlow<List<ClassEntity>> = repository.classes.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val children: StateFlow<List<ChildEntity>> = repository.children.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  // Derived: Areas with Details
  val areasWithDetails: StateFlow<List<AreaWithDetails>> = combine(
    areas,
    governorates,
    children,
    combine(classes, teachers, assignments, supervisors) { cls, tch, ass, sup ->
      Tuple4(cls, tch, ass, sup)
    }
  ) { areaList, govList, childList, tuple ->
    val (classList, teacherList, assignList, supList) = tuple
    val govMap = govList.associateBy { it.id }
    val supMap = supList.associateBy { it.id }

    areaList.map { area ->
      val areaChildren = childList.filter { it.areaId == area.id }
      val boys = areaChildren.count { it.gender == "بنين" }
      val girls = areaChildren.count { it.gender == "بنات" }
      val areaClasses = classList.count { it.areaId == area.id }
      val areaTeachers = teacherList.count { it.areaId == area.id }

      val areaSupervisors = assignList
        .filter { it.areaId == area.id }
        .mapNotNull { supMap[it.supervisorId] }

      AreaWithDetails(
        area = area,
        governorateName = govMap[area.governorateId]?.name ?: "غير محدد",
        childrenCount = areaChildren.size,
        boysCount = boys,
        girlsCount = girls,
        classesCount = areaClasses,
        teachersCount = areaTeachers,
        supervisors = areaSupervisors
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Derived: Children with Details
  val childrenWithDetails: StateFlow<List<ChildWithDetails>> = combine(
    children,
    areas,
    governorates,
    classes,
    teachers
  ) { childList, areaList, govList, classList, teacherList ->
    val areaMap = areaList.associateBy { it.id }
    val govMap = govList.associateBy { it.id }
    val classMap = classList.associateBy { it.id }
    val teacherMap = teacherList.associateBy { it.id }

    childList.map { child ->
      val area = areaMap[child.areaId]
      val gov = area?.let { govMap[it.governorateId] }
      val cls = child.classId?.let { classMap[it] }
      val teacher = cls?.teacherId?.let { teacherMap[it] }

      ChildWithDetails(
        child = child,
        areaName = area?.name ?: "منطقة غير محددة",
        governorateName = gov?.name ?: "محافظة غير محددة",
        className = cls?.name ?: "غير مسجل بفصل",
        teacherName = teacher?.name ?: "غير مسند"
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Derived: Teachers with Details
  val teachersWithDetails: StateFlow<List<TeacherWithDetails>> = combine(
    teachers,
    areas,
    classes,
    children
  ) { teacherList, areaList, classList, childList ->
    val areaMap = areaList.associateBy { it.id }

    teacherList.map { teacher ->
      val area = areaMap[teacher.areaId]
      val teacherClasses = classList.filter { it.teacherId == teacher.id }
      val classIds = teacherClasses.map { it.id }.toSet()
      val kidsCount = childList.count { it.classId in classIds }

      TeacherWithDetails(
        teacher = teacher,
        areaName = area?.name ?: "منطقة غير محددة",
        classesNames = teacherClasses.map { it.name },
        childrenCount = kidsCount
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Derived: Classes with Details
  val classesWithDetails: StateFlow<List<ClassWithDetails>> = combine(
    classes,
    areas,
    governorates,
    teachers,
    children
  ) { classList, areaList, govList, teacherList, childList ->
    val areaMap = areaList.associateBy { it.id }
    val govMap = govList.associateBy { it.id }
    val teacherMap = teacherList.associateBy { it.id }

    classList.map { cls ->
      val area = areaMap[cls.areaId]
      val gov = area?.let { govMap[it.governorateId] }
      val teacher = cls.teacherId?.let { teacherMap[it] }
      val count = childList.count { it.classId == cls.id }

      ClassWithDetails(
        classEntity = cls,
        areaName = area?.name ?: "غير محدد",
        governorateName = gov?.name ?: "غير محدد",
        teacherName = teacher?.name ?: "غير مسند",
        childrenCount = count
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Derived: Supervisors with Details
  val supervisorsWithDetails: StateFlow<List<SupervisorWithDetails>> = combine(
    supervisors,
    governorates,
    assignments,
    areas
  ) { supList, govList, assignList, areaList ->
    val govMap = govList.associateBy { it.id }
    val areaMap = areaList.associateBy { it.id }

    supList.map { sup ->
      val gov = govMap[sup.governorateId]
      val assigned = assignList
        .filter { it.supervisorId == sup.id }
        .mapNotNull { areaMap[it.areaId]?.name }

      SupervisorWithDetails(
        supervisor = sup,
        governorateName = gov?.name ?: "غير محدد",
        assignedAreasNames = assigned
      )
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Derived: Dashboard Stats
  val dashboardStats: StateFlow<DashboardStats> = combine(
    governorates,
    areas,
    supervisors,
    combine(teachers, classes, children, assignments) { tchs, cls, kids, assigns ->
      Tuple4(tchs, cls, kids, assigns)
    }
  ) { govs, areaList, sups, tuple ->
    val (tchs, cls, kids, assigns) = tuple
    val boys = kids.count { it.gender == "بنين" }
    val girls = kids.count { it.gender == "بنات" }
    val orthodox = kids.count { it.denomination == "أرثوذكس" }
    val evangelical = kids.count { it.denomination == "إنجيلي" }
    val catholic = kids.count { it.denomination == "كاثوليك" }
    val other = kids.size - (orthodox + evangelical + catholic)

    val minyaAreaIds = areaList.filter { it.governorateId == "minya" }.map { it.id }.toSet()
    val minyaKids = kids.count { it.areaId in minyaAreaIds }
    val beniKids = kids.size - minyaKids

    DashboardStats(
      totalGovernorates = govs.size,
      totalAreas = areaList.size,
      totalSupervisors = sups.size,
      totalTeachers = tchs.size,
      totalClasses = cls.size,
      totalChildren = kids.size,
      totalAssignments = assigns.size,
      boysCount = boys,
      girlsCount = girls,
      orthodoxCount = orthodox,
      evangelicalCount = evangelical,
      catholicCount = catholic,
      otherCount = maxOf(0, other),
      minyaChildrenCount = minyaKids,
      beniSuefChildrenCount = beniKids
    )
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

  // Global Search
  val searchResults: StateFlow<List<SearchResultItem>> = combine(
    combine(_searchQuery, _searchFilterType, childrenWithDetails) { query, filterType, kids ->
      Triple(query, filterType, kids)
    },
    combine(teachersWithDetails, supervisorsWithDetails, classesWithDetails, areasWithDetails) { tchs, sups, cls, ars ->
      Tuple4(tchs, sups, cls, ars)
    }
  ) { queryKidsTriple, tuple ->
    val (query, filterType, kids) = queryKidsTriple
    val (tchs, sups, cls, ars) = tuple
    val cleanQuery = normalizeArabic(query)
    val results = mutableListOf<SearchResultItem>()

    // Children
    if (filterType == SearchResultType.ALL || filterType == SearchResultType.CHILD) {
      kids.forEach { item ->
        if (cleanQuery.isEmpty() ||
          normalizeArabic(item.child.name).contains(cleanQuery) ||
          normalizeArabic(item.areaName).contains(cleanQuery) ||
          normalizeArabic(item.child.guardianName).contains(cleanQuery) ||
          item.child.guardianPhone.contains(cleanQuery)
        ) {
          results.add(
            SearchResultItem(
              id = item.child.id,
              type = SearchResultType.CHILD,
              title = item.child.name,
              subtitle = "الفصل: ${item.className} • الطائفة: ${item.child.denomination}",
              locationInfo = "${item.governorateName} - ${item.areaName}",
              tag = item.child.gender,
              childRef = item.child
            )
          )
        }
      }
    }

    // Teachers
    if (filterType == SearchResultType.ALL || filterType == SearchResultType.TEACHER) {
      tchs.forEach { item ->
        if (cleanQuery.isEmpty() ||
          normalizeArabic(item.teacher.name).contains(cleanQuery) ||
          normalizeArabic(item.areaName).contains(cleanQuery) ||
          normalizeArabic(item.teacher.specialty).contains(cleanQuery) ||
          item.teacher.phone.contains(cleanQuery)
        ) {
          results.add(
            SearchResultItem(
              id = item.teacher.id,
              type = SearchResultType.TEACHER,
              title = item.teacher.name,
              subtitle = "التخصص: ${item.teacher.specialty} • هاتف: ${item.teacher.phone}",
              locationInfo = item.areaName,
              tag = "خادم / مدرس",
              teacherRef = item.teacher
            )
          )
        }
      }
    }

    // Supervisors
    if (filterType == SearchResultType.ALL || filterType == SearchResultType.SUPERVISOR) {
      sups.forEach { item ->
        if (cleanQuery.isEmpty() ||
          normalizeArabic(item.supervisor.name).contains(cleanQuery) ||
          normalizeArabic(item.governorateName).contains(cleanQuery) ||
          item.supervisor.phone.contains(cleanQuery) ||
          normalizeArabic(item.supervisor.notes).contains(cleanQuery)
        ) {
          results.add(
            SearchResultItem(
              id = item.supervisor.id,
              type = SearchResultType.SUPERVISOR,
              title = item.supervisor.name,
              subtitle = "المناطق: ${item.assignedAreasNames.joinToString("، ")}",
              locationInfo = item.governorateName,
              tag = "مشرف",
              supervisorRef = item.supervisor
            )
          )
        }
      }
    }

    // Classes
    if (filterType == SearchResultType.ALL || filterType == SearchResultType.CLASS) {
      cls.forEach { item ->
        if (cleanQuery.isEmpty() ||
          normalizeArabic(item.classEntity.name).contains(cleanQuery) ||
          normalizeArabic(item.areaName).contains(cleanQuery) ||
          normalizeArabic(item.teacherName).contains(cleanQuery)
        ) {
          results.add(
            SearchResultItem(
              id = item.classEntity.id,
              type = SearchResultType.CLASS,
              title = item.classEntity.name,
              subtitle = "المدرس: ${item.teacherName} • عدد الأطفال: ${item.childrenCount}",
              locationInfo = "${item.governorateName} - ${item.areaName}",
              tag = item.classEntity.stage,
              classRef = item.classEntity
            )
          )
        }
      }
    }

    // Areas
    if (filterType == SearchResultType.ALL || filterType == SearchResultType.AREA) {
      ars.forEach { item ->
        if (cleanQuery.isEmpty() ||
          normalizeArabic(item.area.name).contains(cleanQuery) ||
          normalizeArabic(item.governorateName).contains(cleanQuery)
        ) {
          results.add(
            SearchResultItem(
              id = item.area.id,
              type = SearchResultType.AREA,
              title = item.area.name,
              subtitle = "${item.childrenCount} طفل • ${item.classesCount} فصول • ${item.teachersCount} خدام",
              locationInfo = item.governorateName,
              tag = "منطقة",
              areaRef = item.area
            )
          )
        }
      }
    }

    results
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Arabic Normalization Helper
  private fun normalizeArabic(text: String): String {
    return text.trim()
      .replace("[إأآا]".toRegex(), "ا")
      .replace("ة", "ه")
      .replace("ى", "ي")
      .replace("[ًٌٍَُِّْـ]".toRegex(), "")
      .lowercase()
  }

  // CRUD Operations with Safety and Feedback

  fun saveChild(
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
  ) {
    if (name.isBlank() || areaId.isBlank()) {
      showStatus("يرجى إدخال اسم الطفل واختيار المنطقة")
      return
    }

    viewModelScope.launch {
      val childId = id ?: "ch_${UUID.randomUUID().toString().take(8)}"
      val entity = ChildEntity(
        id = childId,
        name = name.trim(),
        gender = gender,
        denomination = denomination,
        birthYear = birthYear,
        guardianName = guardianName.trim(),
        guardianPhone = guardianPhone.trim(),
        areaId = areaId,
        classId = classId,
        address = address.trim(),
        notes = notes.trim()
      )

      if (id == null) {
        repository.insertChild(entity)
        showStatus("تمت إضافة الطفل بنجاح: ${entity.name}")
      } else {
        repository.updateChild(entity)
        showStatus("تم تحديث بيانات الطفل: ${entity.name}")
      }
      showAddChildDialog.value = false
      editingChild.value = null
    }
  }

  fun deleteChild(child: ChildEntity) {
    viewModelScope.launch {
      repository.deleteChild(child)
      showStatus("تم حذف الطفل: ${child.name}")
      if (_selectedDetailItem.value?.id == child.id) {
        dismissDetail()
      }
    }
  }

  fun saveTeacher(
    id: String?,
    name: String,
    phone: String,
    areaId: String,
    specialty: String,
    notes: String
  ) {
    if (name.isBlank() || areaId.isBlank()) {
      showStatus("يرجى إدخال اسم الخادم/المدرس والمنطقة")
      return
    }

    viewModelScope.launch {
      val teacherId = id ?: "t_${UUID.randomUUID().toString().take(8)}"
      val entity = TeacherEntity(
        id = teacherId,
        name = name.trim(),
        phone = phone.trim(),
        areaId = areaId,
        specialty = specialty.trim(),
        notes = notes.trim()
      )

      if (id == null) {
        repository.insertTeacher(entity)
        showStatus("تمت إضافة الخادم بنجاح: ${entity.name}")
      } else {
        repository.updateTeacher(entity)
        showStatus("تم تحديث بيانات الخادم: ${entity.name}")
      }
      showAddTeacherDialog.value = false
      editingTeacher.value = null
    }
  }

  fun deleteTeacher(teacher: TeacherEntity) {
    viewModelScope.launch {
      repository.deleteTeacher(teacher)
      showStatus("تم حذف الخادم: ${teacher.name}")
      if (_selectedDetailItem.value?.id == teacher.id) {
        dismissDetail()
      }
    }
  }

  fun saveClass(
    id: String?,
    name: String,
    areaId: String,
    stage: String,
    teacherId: String?,
    maxCapacity: Int
  ) {
    if (name.isBlank() || areaId.isBlank()) {
      showStatus("يرجى إدخال اسم الفصل والمنطقة")
      return
    }

    viewModelScope.launch {
      val classId = id ?: "c_${UUID.randomUUID().toString().take(8)}"
      val entity = ClassEntity(
        id = classId,
        name = name.trim(),
        areaId = areaId,
        stage = stage,
        teacherId = teacherId,
        maxCapacity = maxCapacity
      )

      if (id == null) {
        repository.insertClass(entity)
        showStatus("تمت إضافة الفصل بنجاح: ${entity.name}")
      } else {
        repository.updateClass(entity)
        showStatus("تم تحديث بيانات الفصل: ${entity.name}")
      }
      showAddClassDialog.value = false
      editingClass.value = null
    }
  }

  fun deleteClass(classEntity: ClassEntity) {
    viewModelScope.launch {
      // Check if children are assigned
      val hasKids = children.value.any { it.classId == classEntity.id }
      if (hasKids) {
        showStatus("تنبيه: لا يمكن حذف الفصل لوجود أطفال مسجلين به")
        return@launch
      }
      repository.deleteClass(classEntity)
      showStatus("تم حذف الفصل: ${classEntity.name}")
      if (_selectedDetailItem.value?.id == classEntity.id) {
        dismissDetail()
      }
    }
  }

  fun saveSupervisor(
    id: String?,
    name: String,
    phone: String,
    email: String,
    governorateId: String,
    notes: String,
    assignedAreaIds: List<String>
  ) {
    if (name.isBlank()) {
      showStatus("يرجى إدخال اسم المشرف")
      return
    }

    viewModelScope.launch {
      val supervisorId = id ?: "sup_${UUID.randomUUID().toString().take(8)}"
      val entity = SupervisorEntity(
        id = supervisorId,
        name = name.trim(),
        phone = phone.trim(),
        email = email.trim(),
        governorateId = governorateId,
        notes = notes.trim()
      )

      if (id == null) {
        repository.insertSupervisor(entity)
      } else {
        repository.updateSupervisor(entity)
      }

      // Update area assignments
      assignedAreaIds.forEach { areaId ->
        repository.assignSupervisorToArea(supervisorId, areaId, "مشرف المنطقة")
      }

      showStatus("تم حفظ بيانات المشرف: ${entity.name}")
      showAddSupervisorDialog.value = false
      editingSupervisor.value = null
    }
  }

  fun deleteSupervisor(supervisor: SupervisorEntity) {
    viewModelScope.launch {
      repository.deleteSupervisor(supervisor)
      showStatus("تم حذف المشرف: ${supervisor.name}")
      if (_selectedDetailItem.value?.id == supervisor.id) {
        dismissDetail()
      }
    }
  }

  // Session & Authentication
  fun login(username: String, role: UserRole) {
    val displayName = when (role) {
      UserRole.ADMIN -> "أ. روماني عاطف (مدير عام النظام)"
      UserRole.SUPERVISOR -> "أ. مينا سمير (مشرف قطاع)"
      UserRole.TEACHER -> "خادم مسجل (مرحلة ابتدائي)"
    }
    _userSession.value = UserSession(
      username = username,
      fullName = displayName,
      role = role,
      isLoggedIn = true
    )
    showStatus("مرحبًا بك: $displayName")
  }

  fun logout() {
    _userSession.value = UserSession(isLoggedIn = false)
    showStatus("تم تسجيل الخروج بنجاح")
  }
}
