package com.leonardo.cybersafecheck

import com.leonardo.cybersafecheck.AssessmentDao
import com.leonardo.cybersafecheck.AssessmentEntity
import com.leonardo.cybersafecheck.RiskAnswerEntity
import com.leonardo.cybersafecheck.RiskDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RiskRepository(
    private val riskDao: RiskDao,
    private val assessmentDao: AssessmentDao
) {
    suspend fun getAll(): List<RiskAnswerEntity> = withContext(Dispatchers.IO) {
        val existing = riskDao.getAll()
        existing.ifEmpty {
            seedDefaultData()
            riskDao.getAll()
        }
    }

    suspend fun getById(itemId: String): RiskAnswerEntity? = withContext(Dispatchers.IO) {
        riskDao.getById(itemId)
    }

    suspend fun setFlagged(itemId: String, flagged: Boolean) = withContext(Dispatchers.IO) {
        riskDao.updateFlagged(itemId, flagged)
    }

    suspend fun saveAssessment(flaggedCount: Int, totalCount: Int) = withContext(Dispatchers.IO) {
        val assessment = AssessmentEntity(
            timestamp = System.currentTimeMillis(),
            flaggedCount = flaggedCount,
            totalCount = totalCount
        )
        assessmentDao.insertAssessment(assessment)
    }
    suspend fun clearFlags() = withContext(Dispatchers.IO) {
        riskDao.clearAllFlags()
    }
    private suspend fun seedDefaultData() = withContext(Dispatchers.IO) {
        val seedItems = listOf(
            RiskAnswerEntity(
                "1",
                "I reuse the same password on more than one site.",
                "PASSWORDS",
                "Reusing passwords across services enables credential-stuffing attacks if one account is compromised."
            ),

            RiskAnswerEntity(
                "2",
                "I've accepted a friend request from someone I don't know.",
                "SOCIAL_MEDIA",
                "Unknown social media connections can scrape personal information for social engineering or phishing."
            ),

            RiskAnswerEntity(
                "3",
                "I click on links in unexpected SMS messages or emails.",
                "SCAMS",
                "Unverified links frequently point to phishing pages engineered to harvest credentials or malware."
            ),

            RiskAnswerEntity(
                "4",
                "I do not use two-factor authentication (2FA) on critical accounts.",
                "PASSWORDS",
                "Two-factor authentication adds an essential security layer beyond passwords alone."
            ),

            RiskAnswerEntity(
                "5",
                "I share personal details like my full birthday publicly.",
                "SOCIAL_MEDIA",
                "Publicly visible personal details make identity theft and recovery-question resets easier."
            ),

            RiskAnswerEntity(
                "6",
                "I engage with or reply to aggressive messages online.",
                "CYBERBULLYING",
                "Direct engagement with hostile users escalates online harassment and toxic interactions."
            ),

            RiskAnswerEntity(
                "7",
                "I download apps from unverified third-party websites.",
                "SCAMS",
                "Unofficial app stores bypass security vetting, greatly increasing malware infection risks."
            ),

            RiskAnswerEntity(
                "8",
                "I leave my phone unlocked in public places.",
                "PASSWORDS",
                "Leaving devices unlocked allows physical unauthorized access to private messages and banking data."
            )
        )
        riskDao.insertAll(seedItems)
    }
}