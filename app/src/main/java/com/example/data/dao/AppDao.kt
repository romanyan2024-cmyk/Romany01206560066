package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AreaEntity
import com.example.data.model.ChildEntity
import com.example.data.model.ClassEntity
import com.example.data.model.GovernorateEntity
import com.example.data.model.SupervisorAreaAssignmentEntity
import com.example.data.model.SupervisorEntity
import com.example.data.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
  // Governorates
  @Query("SELECT * FROM governorates ORDER BY orderIndex ASC")
  fun getAllGovernorates(): Flow<List<GovernorateEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertGovernorates(governorates: List<GovernorateEntity>)

  // Areas
  @Query("SELECT * FROM areas ORDER BY orderIndex ASC")
  fun getAllAreas(): Flow<List<AreaEntity>>

  @Query("SELECT * FROM areas WHERE governorateId = :governorateId ORDER BY orderIndex ASC")
  fun getAreasByGovernorate(governorateId: String): Flow<List<AreaEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAreas(areas: List<AreaEntity>)

  // Supervisors
  @Query("SELECT * FROM supervisors ORDER BY name ASC")
  fun getAllSupervisors(): Flow<List<SupervisorEntity>>

  @Query("SELECT * FROM supervisors WHERE id = :id LIMIT 1")
  suspend fun getSupervisorById(id: String): SupervisorEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSupervisor(supervisor: SupervisorEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertSupervisors(supervisors: List<SupervisorEntity>)

  @Update
  suspend fun updateSupervisor(supervisor: SupervisorEntity)

  @Delete
  suspend fun deleteSupervisor(supervisor: SupervisorEntity)

  // Supervisor Assignments
  @Query("SELECT * FROM supervisor_area_assignments")
  fun getAllAssignments(): Flow<List<SupervisorAreaAssignmentEntity>>

  @Query("SELECT * FROM supervisor_area_assignments WHERE supervisorId = :supervisorId")
  fun getAssignmentsBySupervisor(supervisorId: String): Flow<List<SupervisorAreaAssignmentEntity>>

  @Query("SELECT * FROM supervisor_area_assignments WHERE areaId = :areaId")
  fun getAssignmentsByArea(areaId: String): Flow<List<SupervisorAreaAssignmentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssignment(assignment: SupervisorAreaAssignmentEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAssignments(assignments: List<SupervisorAreaAssignmentEntity>)

  @Query("DELETE FROM supervisor_area_assignments WHERE supervisorId = :supervisorId AND areaId = :areaId")
  suspend fun removeAssignment(supervisorId: String, areaId: String)

  @Query("DELETE FROM supervisor_area_assignments WHERE supervisorId = :supervisorId")
  suspend fun deleteAssignmentsForSupervisor(supervisorId: String)

  // Teachers
  @Query("SELECT * FROM teachers ORDER BY name ASC")
  fun getAllTeachers(): Flow<List<TeacherEntity>>

  @Query("SELECT * FROM teachers WHERE areaId = :areaId ORDER BY name ASC")
  fun getTeachersByArea(areaId: String): Flow<List<TeacherEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTeacher(teacher: TeacherEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertTeachers(teachers: List<TeacherEntity>)

  @Update
  suspend fun updateTeacher(teacher: TeacherEntity)

  @Delete
  suspend fun deleteTeacher(teacher: TeacherEntity)

  // Classes
  @Query("SELECT * FROM classes ORDER BY name ASC")
  fun getAllClasses(): Flow<List<ClassEntity>>

  @Query("SELECT * FROM classes WHERE areaId = :areaId ORDER BY name ASC")
  fun getClassesByArea(areaId: String): Flow<List<ClassEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClass(classEntity: ClassEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertClasses(classes: List<ClassEntity>)

  @Update
  suspend fun updateClass(classEntity: ClassEntity)

  @Delete
  suspend fun deleteClass(classEntity: ClassEntity)

  // Children
  @Query("SELECT * FROM children ORDER BY name ASC")
  fun getAllChildren(): Flow<List<ChildEntity>>

  @Query("SELECT * FROM children WHERE areaId = :areaId ORDER BY name ASC")
  fun getChildrenByArea(areaId: String): Flow<List<ChildEntity>>

  @Query("SELECT * FROM children WHERE classId = :classId ORDER BY name ASC")
  fun getChildrenByClass(classId: String): Flow<List<ChildEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChild(child: ChildEntity)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertChildren(children: List<ChildEntity>)

  @Update
  suspend fun updateChild(child: ChildEntity)

  @Delete
  suspend fun deleteChild(child: ChildEntity)

  @Query("SELECT COUNT(*) FROM children")
  suspend fun getChildrenCount(): Int

  @Query("SELECT COUNT(*) FROM teachers")
  suspend fun getTeachersCount(): Int

  @Query("SELECT COUNT(*) FROM classes")
  suspend fun getClassesCount(): Int

  @Query("SELECT COUNT(*) FROM supervisors")
  suspend fun getSupervisorsCount(): Int
}
