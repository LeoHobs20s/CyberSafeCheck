package com.leonardo.cybersafecheck

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ScoreDialogFragment : DialogFragment() {
    private lateinit var repository: RiskRepository
    private var flaggedCount = 0
    private var totalCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val db = CyberSafeDatabase.getDatabase(requireContext())

        repository = RiskRepository(db.riskDao(), db.assessmentDao())
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Calculating Risk Score...")
            .setMessage("Loading data...")
            .setPositiveButton("Save to History") { _, _ ->
                lifecycleScope.launch { repository.saveAssessment(flaggedCount, totalCount)
                }
            }
            .setNegativeButton("Close", null)

        val dialog = builder.create()

        lifecycleScope.launch {
            val items = repository.getAll()

            val totalCount = items.size
            val flaggedCount = items.count { it.isFlagged }


            val categoryBreakdown = items.groupBy { it.category }
                .map { (category, list) ->
                    val flagged = list.count { it.isFlagged }
                    "• $category: $flagged/${list.size} flagged"
                }.joinToString("\n")

            val message = "Flagged Behaviors: $flaggedCount / $totalCount\n\nCategory Breakdown:\n$categoryBreakdown"

            dialog.setMessage(message)
            dialog.setTitle("Your Cybersecurity Risk Score")
        }

        return dialog
    }

}