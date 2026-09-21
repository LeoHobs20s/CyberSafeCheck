package com.leonardo.cybersafecheck

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface AssessmentDao {
    @Insert
    suspend fun insertAssessment(assessment: AssessmentEntity)
    @Query("SELECT * FROM assessments ORDER BY timestamp DESC")
    suspend fun getAllAssessments(): List<AssessmentEntity>
}