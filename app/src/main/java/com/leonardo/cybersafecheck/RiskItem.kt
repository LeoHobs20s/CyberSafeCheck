package com.leonardo.cybersafecheck

import java.util.UUID

data class RiskItem(
    var id: UUID = UUID.randomUUID(),
    var questionText: String,
    var category: RiskCategory,
    var isYes: Boolean = false,
    var explanation: String
)
