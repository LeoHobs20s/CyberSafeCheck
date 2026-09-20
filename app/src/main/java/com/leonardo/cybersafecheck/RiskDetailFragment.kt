package com.leonardo.cybersafecheck

import android.adservices.adid.AdId
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.UUID

private const val ARG_RISK_ID = "risk_id"
class RiskDetailFragment: Fragment() {

    private var riskItem: RiskItem? = null

    private lateinit var categoryTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var explanationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        val riskId = arguments?.getSerializable(ARG_RISK_ID) as? UUID

        if(riskId != null){
            riskItem = RiskLab.get().getRiskItem(riskId)
        }
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

        riskItem?.let { item ->
            categoryTextView.text = "CATEGORY: ${item.category.name}"
            questionTextView.text = item.questionText
            explanationTextView.text = item.explanation

        }
        return view
    }

    companion object {
        fun newInstance(riskId: UUID): RiskDetailFragment{
            val args = Bundle().apply{
                putSerializable(ARG_RISK_ID, riskId)
            }

            return RiskDetailFragment().apply{
                arguments = args
            }
        }
    }
}