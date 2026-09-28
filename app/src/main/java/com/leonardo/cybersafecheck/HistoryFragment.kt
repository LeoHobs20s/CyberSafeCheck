package com.leonardo.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leonardo.cybersafecheck.AssessmentEntity
import com.leonardo.cybersafecheck.CyberSafeDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date import java.util.Locale

class HistoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var repository: RiskRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = CyberSafeDatabase.getDatabase(requireContext())
        repository = RiskRepository(db.riskDao(), db.assessmentDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.history_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            val assessments = withContext(Dispatchers.IO) {
                dbAssessmentFetch()
            }
            recyclerView.adapter = HistoryAdapter(assessments)
        }
        return view
    }

    private suspend fun dbAssessmentFetch(): List<AssessmentEntity> {
        return CyberSafeDatabase.getDatabase(requireContext()).assessmentDao().getAllAssessments()
    }

    private inner class HistoryHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dateText: TextView = itemView.findViewById(R.id.history_date_text)
        private val scoreText: TextView = itemView.findViewById(R.id.history_score_text)

        fun bind(assessment: AssessmentEntity) {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
            dateText.text = dateFormat.format(Date(assessment.timestamp))
            scoreText.text = "Flagged: ${assessment.flaggedCount}/${assessment.totalCount}"
        }
    }

    private inner class HistoryAdapter(private val items: List<AssessmentEntity>) :
        RecyclerView.Adapter<HistoryHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
            val view = layoutInflater.inflate(R.layout.list_item_history, parent, false)
            return HistoryHolder(view)
        }
        override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
            holder.bind(items[position])
        }
        override fun getItemCount(): Int = items.size
    }
}