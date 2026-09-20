package com.leonardo.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChecklistFragment: Fragment() {
    private lateinit var riskRecyclerView: RecyclerView
    private var adapter: RiskAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checklist, container, false)

        riskRecyclerView = view.findViewById(R.id.risk_recycler_view)
        riskRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val riskItems = RiskLab.get().riskItems

        if(adapter == null) {
            adapter = RiskAdapter(riskItems)
            riskRecyclerView.adapter = adapter
        }
        else{
            adapter?.notifyDataSetChanged()
        }
    }

    private inner class RiskHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener
    {
        private lateinit var riskItem: RiskItem
        private var questionTextView: TextView = itemView.findViewById(R.id.risk_question_text)
        private var yesSwitch: Switch = itemView.findViewById(R.id.risk_yes_switch)

        init {
            questionTextView.setOnClickListener(this)
        }

        fun bind(item: RiskItem) {
            riskItem = item
            questionTextView.text = riskItem.questionText

            yesSwitch.setOnCheckedChangeListener(null)
            yesSwitch.isChecked = riskItem.isYes
            yesSwitch.setOnCheckedChangeListener { _, isChecked ->
                riskItem.isYes = isChecked
            }
        }

        override fun onClick(v: View?) {
            val fragment = RiskDetailFragment.newInstance(riskItem.id)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

    }

    private inner class RiskAdapter(var riskItems: List<RiskItem>) : RecyclerView.Adapter<RiskHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RiskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_risk, parent, false)
            return RiskHolder(view)
        }

        override fun onBindViewHolder(holder: RiskHolder, position: Int){
            val item = riskItems[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int = riskItems.size
    }

}