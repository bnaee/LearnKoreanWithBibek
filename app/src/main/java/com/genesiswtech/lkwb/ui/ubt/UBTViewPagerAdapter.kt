package com.genesiswtech.lkwb.ui.ubt

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseFragment

class UBTViewPagerAdapter(fm: FragmentManager, var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> UBTTestFragment()
            1 -> MostPurchaseFragment()

            else -> UBTTestFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }
}
