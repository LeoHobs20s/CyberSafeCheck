package com.leonardo.cybersafecheck

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val flaggedCount: Int,
    val totalCount: Int
)
