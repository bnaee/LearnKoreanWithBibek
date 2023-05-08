package com.genesiswtech.lkwb.ui.ubtQuestion

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionDataResponse
import com.genesiswtech.lkwb.utils.LKWBConstants


class UBTQuestionFragmentAdapter internal constructor(
    fm: FragmentManager?,
    private val mNumOfTabs: Int,
    private val ubtQuestionDataResponseList: ArrayList<UBTQuestionDataResponse>
) :
    FragmentStatePagerAdapter(fm!!) {

    private var list: ArrayList<UBTQuestionDataResponse> = ArrayList()

    override fun getItem(position: Int): Fragment {
        list.addAll(ubtQuestionDataResponseList)
        val b = Bundle()
        b.putInt(LKWBConstants.DATA_POSITION, position)
        b.putParcelableArrayList(
            LKWBConstants.DATA_VALUE,
            list as ArrayList<out Parcelable?>?
        )
        val frag: Fragment = UBTQuestionFragment.newInstance()
        frag.arguments = b
        return frag
    }

    override fun getCount(): Int {
        return mNumOfTabs
    }
}