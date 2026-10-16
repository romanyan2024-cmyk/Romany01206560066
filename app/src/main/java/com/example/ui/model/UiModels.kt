package com.example.ui.model

import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity

enum class AppTab(val title: String) {
  DASHBOARD("لوحة التحكم"),
  AREAS("استعراض المناطق"),
  CHILDREN("الأطفال"),
  CLASSES("الفصول"),
  TEACHERS("الخدام والمدرسون"),
  SUPERVISORS("المشرفون"),
  SEARCH("البحث العام"),
  REPORTS("التقارير")
}

enum class UserRole(val titleArabic: String) {
  ADMIN("مدير عام النظام"),
  SUPERVISOR("مشرف قطاع / محافظة"),
  TEACHER("خادم / مدرس مسجل")
}

data class UserSession(
  val username: String = "admin",
  val fullName: String = "أ. روماني عاطف (مدير النظام)",
  val role: UserRole = UserRole.ADMIN,
  val isLoggedIn: Boolean = true
)

data class AreaWithDetails(
  val area: AreaEntity,
  val governorateName: String,
  val childrenCount: Int,
  val boysCount: Int,
  val girlsCount: Int,
  val classesCount: Int,
  val teachersCount: Int,
  val supervisors: List<SupervisorEntity>
)

data class ChildWithDetails(
  val child: ChildEntity,
  val areaName: String,
  val governorateName: String,
  val className: String,
  val teacherName: String
)

data class ClassWithDetails(
  val classEntity: ClassEntity,
  val areaName: String,
  val governorateName: String,
  val teacherName: String,
  val childrenCount: Int
)

data class TeacherWithDetails(
  val teacher: TeacherEntity,
  val areaName: String,
  val classesNames: List<String>,
  val childrenCount: Int
)

data class SupervisorWithDetails(
  val supervisor: SupervisorEntity,
  val governorateName: String,
  val assignedAreasNames: List<String>
)

enum class SearchResultType(val label: String) {
  ALL("الكل"),
  CHILD("طفل"),
  TEACHER("خادم / مدرس"),
  SUPERVISOR("مشرف"),
  CLASS("فصل"),
  AREA("منطقة")
}

data class SearchResultItem(
  val id: String,
  val type: SearchResultType,
  val title: String,
  val subtitle: String,
  val locationInfo: String,
  val tag: String,
  val childRef: ChildEntity? = null,
  val teacherRef: TeacherEntity? = null,
  val supervisorRef: SupervisorEntity? = null,
  val classRef: ClassEntity? = null,
  val areaRef: AreaEntity? = null
)

data class DashboardStats(
  val totalGovernorates: Int = 2,
  val totalAreas: Int = 11,
  val totalSupervisors: Int = 0,
  val totalTeachers: Int = 0,
  val totalClasses: Int = 0,
  val totalChildren: Int = 0,
  val totalAssignments: Int = 0,
  val boysCount: Int = 0,
  val girlsCount: Int = 0,
  val orthodoxCount: Int = 0,
  val evangelicalCount: Int = 0,
  val catholicCount: Int = 0,
  val otherCount: Int = 0,
  val minyaChildrenCount: Int = 0,
  val beniSuefChildrenCount: Int = 0
)
