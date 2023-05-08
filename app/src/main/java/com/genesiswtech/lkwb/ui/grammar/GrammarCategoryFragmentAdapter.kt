package com.genesiswtech.lkwb.ui.grammar

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryDataResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse

class GrammarCategoryFragmentAdapter internal constructor(
    fm: FragmentManager,
    private val tabCount: Int,
    private val grammarCategoryDataResponseList: ArrayList<GrammarCategoryDataResponse>
) : FragmentPagerAdapter(fm) {
    private var list: ArrayList<GrammarCategoryDataResponse> = ArrayList()
    override fun getItem(position: Int): Fragment {

        list.addAll(grammarCategoryDataResponseList)
        val b = Bundle()
        b.putParcelableArrayList(
            "ModelAddress",
            list[position].grammars as ArrayList<out Parcelable?>?
        )
        val frag: Fragment = GrammarCategoryFragment
            .newInstance()
        frag.arguments = b
        return frag

    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }
}
