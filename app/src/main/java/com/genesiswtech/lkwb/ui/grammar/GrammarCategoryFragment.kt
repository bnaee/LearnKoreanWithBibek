package com.genesiswtech.lkwb.ui.grammar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentGrammarCategoryBinding
import com.genesiswtech.lkwb.ui.grammar.model.Grammar
import com.genesiswtech.lkwb.ui.grammar.view.IGrammarCategoryView
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.utils.LKWBConstants


class GrammarCategoryFragment : Fragment(R.layout.fragment_grammar_category), IGrammarCategoryView {

    private lateinit var grammarCategoryBinding: FragmentGrammarCategoryBinding
    private var list: ArrayList<Grammar> = ArrayList()

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentGrammarCategoryBinding.bind(itemView)
        grammarCategoryBinding = binding
        binding.categoryGrammarHandler = this
        getBundle()
    }


    private fun getBundle() {
        list = requireArguments().getSerializable("ModelAddress") as ArrayList<Grammar>
        setGrammarCategoryAdapter(list)
    }


    private fun setGrammarCategoryAdapter(data: ArrayList<Grammar>) {
        grammarCategoryBinding.grammarCategoryRV.layoutManager = LinearLayoutManager(activity)
        val grammarCategoryAdapter = GrammarCategoryAdapter(requireContext(), data)
        grammarCategoryBinding.grammarCategoryRV.adapter = grammarCategoryAdapter
        grammarCategoryAdapter.onItemClick = {
            val intent = Intent(activity, GrammarWordActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.word)
            startActivity(intent)
        }

        onNoData(grammarCategoryAdapter.itemCount)
    }

    private fun onNoData(size: Int) {
        if (size == 0)
            grammarCategoryBinding.noDataTV.visibility = View.VISIBLE
        else
            grammarCategoryBinding.noDataTV.visibility = View.GONE
    }

    companion object {
        fun newInstance(): GrammarCategoryFragment {
            return GrammarCategoryFragment()
        }
    }
}



