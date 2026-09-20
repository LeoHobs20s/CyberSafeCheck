package com.leonardo.cybersafecheck

import java.util.UUID

class RiskLab private constructor() {

    val riskItems: List<RiskItem> = listOf(
        RiskItem(
            questionText = "I reused the same password on more than one website.",
            category = RiskCategory.PASSWORDS,
            explanation = "Reusing passwords across multiple accounts allows attackers who compromise one service to access all your other accounts using credential stuffing."
        ),
        RiskItem(
            questionText = "I have accepted a friend request from someone I don't know.",
            category = RiskCategory.SOCIAL_MEDIA,
            explanation = "Unknown social media connections can scrape your personal information, track your location, or target you with social engineering scams."
        ),
        RiskItem(
            questionText = "I click on links in unexpected SMS messages or emails.",
            category = RiskCategory.SCAMS,
            explanation = "Unverified links can direct you to fraudulent phishing websites designed to steal credentials or download malware onto your device."
        ),
        RiskItem(
            questionText = "I engage with or reply to aggressive or abusive messages online.",
            category = RiskCategory.CYBERBULLYING,
            explanation = "Responding directly to abusive or hostile users usually escalates toxic behavior. It is safer to block, document, and report them."
        )
    )

    fun getRiskItem(id: UUID): RiskItem? {
        return riskItems.firstOrNull {it.id == id}
    }

    companion object {
        private var INSTANCE: RiskLab? = null

        fun get(): RiskLab {
            if(INSTANCE == null){
                INSTANCE = RiskLab()
            }
            return INSTANCE!!
        }
    }
}