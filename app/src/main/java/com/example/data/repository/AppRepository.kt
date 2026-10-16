package com.example.data.repository

import com.example.data.dao.AppDao
import com.example.data.db.SeedData
import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.GovernorateEntity
import com.example.data.model.SupervisorAreaAssignmentEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
  val governorates: Flow<List<GovernorateEntity>> = dao.getAllGovernorates()
  val areas: Flow<List<AreaEntity>> = dao.getAllAreas()
  val supervisors: Flow<List<SupervisorEntity>> = dao.getAllSupervisors()
  val assignments: Flow<List<SupervisorAreaAssignmentEntity>> = dao.getAllAssignments()
  val teachers: Flow<List<TeacherEntity>> = dao.getAllTeachers()
  val classes: Flow<List<ClassEntity>> = dao.getAllClasses()
  val children: Flow<List<ChildEntity>> = dao.getAllChildren()

  suspend fun ensureDataSeeded() {
    val count = dao.getChildrenCount()
    if (count == 0) {
      SeedData.populateInitialData(dao)
    }
  }

  // Children
  suspend fun insertChild(child: ChildEntity) = dao.insertChild(child)
  suspend fun updateChild(child: ChildEntity) = dao.updateChild(child)
  suspend fun deleteChild(child: ChildEntity) = dao.deleteChild(child)

  // Teachers
  suspend fun insertTeacher(teacher: TeacherEntity) = dao.insertTeacher(teacher)
  suspend fun updateTeacher(teacher: TeacherEntity) = dao.updateTeacher(teacher)
  suspend fun deleteTeacher(teacher: TeacherEntity) = dao.deleteTeacher(teacher)

  // Classes
  suspend fun insertClass(classEntity: ClassEntity) = dao.insertClass(classEntity)
  suspend fun updateClass(classEntity: ClassEntity) = dao.updateClass(classEntity)
  suspend fun deleteClass(classEntity: ClassEntity) = dao.deleteClass(classEntity)

  // Supervisors
  suspend fun insertSupervisor(supervisor: SupervisorEntity) = dao.insertSupervisor(supervisor)
  suspend fun updateSupervisor(supervisor: SupervisorEntity) = dao.updateSupervisor(supervisor)
  suspend fun deleteSupervisor(supervisor: SupervisorEntity) = dao.deleteSupervisor(supervisor)

  // Assignments
  suspend fun assignSupervisorToArea(supervisorId: String, areaId: String, roleTitle: String) {
    dao.insertAssignment(
      SupervisorAreaAssignmentEntity(
        supervisorId = supervisorId,
        areaId = areaId,
        roleTitle = roleTitle
      )
    )
  }

  suspend fun removeAssignment(supervisorId: String, areaId: String) {
    dao.removeAssignment(supervisorId, areaId)
  }
}
