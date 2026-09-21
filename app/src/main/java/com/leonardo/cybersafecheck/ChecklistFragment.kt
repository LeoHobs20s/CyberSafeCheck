package com.leonardo.cybersafecheck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ChecklistFragment : Fragment() {
    private lateinit var riskRecyclerView: RecyclerView
    private lateinit var repository: RiskRepository
    private var adapter: RiskAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = CyberSafeDatabase.getDatabase(requireContext())
        repository = RiskRepository(db.riskDao(), db.assessmentDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_checklist, container, false)

        riskRecyclerView = view.findViewById(R.id.risk_recycler_view)
        riskRecyclerView.layoutManager = LinearLayoutManager(context)

        loadData()
        return view
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val items = repository.getAll()

            if (adapter == null) {
                adapter = RiskAdapter(items)
                riskRecyclerView.adapter = adapter
            } else {
                adapter?.updateItems(items)
            }
        }
    }

    private inner class RiskHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var item: RiskAnswerEntity
        private val questionTextView: TextView = itemView.findViewById(R.id.risk_question_text)
        private val yesSwitch: SwitchCompat = itemView.findViewById(R.id.risk_yes_switch)

        init {
            questionTextView.setOnClickListener(this)
        }

        fun bind(entity: RiskAnswerEntity) {
            item = entity
            questionTextView.text = item.question

            yesSwitch.setOnCheckedChangeListener(null)
            yesSwitch.isChecked = item.isFlagged

            yesSwitch.setOnCheckedChangeListener { _, isChecked ->
                item.isFlagged = isChecked
                lifecycleScope.launch {
                    repository.setFlagged(item.itemId, isChecked)
                }
            }
        }

        override fun onClick(v: View?) {
            val fragment = RiskDetailFragment.newInstance(item.itemId)

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private inner class RiskAdapter(private var items: List<RiskAnswerEntity>): RecyclerView.Adapter<RiskHolder>() {

        fun updateItems(newItems: List<RiskAnswerEntity>){
            items = newItems
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): RiskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_risk, parent, false)
            return RiskHolder(view)
        }

        override fun onBindViewHolder(
            holder: RiskHolder,
            position: Int
        ) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int {
            return items.size
        }
    }

}