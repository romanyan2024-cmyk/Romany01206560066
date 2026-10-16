package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "governorates")
data class GovernorateEntity(
  @PrimaryKey val id: String,
  val name: String,
  val orderIndex: Int
)

@Entity(
  tableName = "areas",
  foreignKeys = [
    ForeignKey(
      entity = GovernorateEntity::class,
      parentColumns = ["id"],
      childColumns = ["governorateId"],
      onDelete = ForeignKey.RESTRICT
    )
  ],
  indices = [Index("governorateId")]
)
data class AreaEntity(
  @PrimaryKey val id: String,
  val governorateId: String,
  val name: String,
  val orderIndex: Int
)

@Entity(tableName = "supervisors")
data class SupervisorEntity(
  @PrimaryKey val id: String,
  val name: String,
  val phone: String,
  val email: String,
  val governorateId: String,
  val notes: String = ""
)

@Entity(
  tableName = "supervisor_area_assignments",
  foreignKeys = [
    ForeignKey(
      entity = SupervisorEntity::class,
      parentColumns = ["id"],
      childColumns = ["supervisorId"],
      onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
      entity = AreaEntity::class,
      parentColumns = ["id"],
      childColumns = ["areaId"],
      onDelete = ForeignKey.CASCADE
    )
  ],
  indices = [Index("supervisorId"), Index("areaId")]
)
data class SupervisorAreaAssignmentEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val supervisorId: String,
  val areaId: String,
  val roleTitle: String = "مشرف المنطقة"
)

@Entity(
  tableName = "teachers",
  foreignKeys = [
    ForeignKey(
      entity = AreaEntity::class,
      parentColumns = ["id"],
      childColumns = ["areaId"],
      onDelete = ForeignKey.RESTRICT
    )
  ],
  indices = [Index("areaId")]
)
data class TeacherEntity(
  @PrimaryKey val id: String,
  val name: String,
  val phone: String,
  val areaId: String,
  val specialty: String,
  val notes: String = ""
)

@Entity(
  tableName = "classes",
  foreignKeys = [
    ForeignKey(
      entity = AreaEntity::class,
      parentColumns = ["id"],
      childColumns = ["areaId"],
      onDelete = ForeignKey.RESTRICT
    )
  ],
  indices = [Index("areaId")]
)
data class ClassEntity(
  @PrimaryKey val id: String,
  val name: String,
  val areaId: String,
  val stage: String,
  val teacherId: String? = null,
  val maxCapacity: Int = 30
)

@Entity(
  tableName = "children",
  foreignKeys = [
    ForeignKey(
      entity = AreaEntity::class,
      parentColumns = ["id"],
      childColumns = ["areaId"],
      onDelete = ForeignKey.RESTRICT
    )
  ],
  indices = [Index("areaId"), Index("classId")]
)
data class ChildEntity(
  @PrimaryKey val id: String,
  val name: String,
  val gender: String, // "بنين" or "بنات"
  val denomination: String, // "أرثوذكس", "إنجيلي", "كاثوليك", "أخرى"
  val birthYear: Int,
  val guardianName: String,
  val guardianPhone: String,
  val areaId: String,
  val classId: String? = null,
  val address: String = "",
  val notes: String = "",
  val registeredDate: String = "2026-01-15"
)
