package com.leonardo.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.leonardo.cybersafecheck.CyberSafeDatabase
import kotlinx.coroutines.launch

private const val ARG_ITEM_ID = "item_id"

class RiskDetailFragment : Fragment() {

    private var itemId: String? = null
    private lateinit var repository: RiskRepository
    private lateinit var categoryTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var explanationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemId = arguments?.getString(ARG_ITEM_ID)

        val db = CyberSafeDatabase.getDatabase(requireContext())
        repository = RiskRepository(db.riskDao(), db.assessmentDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_risk_detail, container, false)

        categoryTextView = view.findViewById(R.id.detail_category_text)
        questionTextView = view.findViewById(R.id.detail_question_text)
        explanationTextView = view.findViewById(R.id.detail_explanation_text)

        itemId?.let { id ->
            lifecycleScope.launch {
                val entity = repository.getById(id)

                entity?.let { item ->
                    categoryTextView.text = "CATEGORY: ${item.category}"
                    questionTextView.text = item.question
                    explanationTextView.text = item.explanation
                }
            }
        }
        return view
    }

    companion object {
        fun newInstance(itemId: String): RiskDetailFragment {
            val args = Bundle().apply { putString(ARG_ITEM_ID, itemId) }

            return RiskDetailFragment().apply { arguments = args }
        }
    }
}