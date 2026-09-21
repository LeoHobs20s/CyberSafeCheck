package com.leonardo.cybersafecheck

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface RiskDao {
    @Query("SELECT * FROM risk_answers")
    suspend fun getAll(): List<RiskAnswerEntity>
    @Query("SELECT * FROM risk_answers WHERE itemId = :itemId")
    suspend fun getById(itemId: String): RiskAnswerEntity?
    @Query("UPDATE risk_answers SET isFlagged = :flagged WHERE itemId = :itemId")
    suspend fun updateFlagged(itemId: String, flagged: Boolean)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<RiskAnswerEntity>)

    @Query("UPDATE risk_answers SET isFlagged = 0")
    suspend fun clearAllFlags()
}