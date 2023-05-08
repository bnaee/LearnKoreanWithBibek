package com.genesiswtech.lkwb.ui.ubt

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.databinding.FragmentUbtBinding
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class UBTFragment : Fragment(R.layout.fragment_ubt) {

     private lateinit var fragmentUbtBinding: FragmentUbtBinding
    private var firstRun: Boolean? = false

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val binding = FragmentUbtBinding.bind(itemView)
        fragmentUbtBinding = binding
        navigateToPurchaseTab()

        setupTabLayout()
        setupViewPager()
        fragmentUbtBinding.ubtVP.currentItem = 0
    }

    private fun setupViewPager() {
        fragmentUbtBinding.ubtVP.apply {
            adapter = UBTViewPagerAdapter(
                requireActivity().supportFragmentManager,
                fragmentUbtBinding.ubtTL.tabCount
            )
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(fragmentUbtBinding.ubtTL))
        }
    }

    private fun setupTabLayout() {
        fragmentUbtBinding.ubtTL.apply {
            addTab(this.newTab().setText(getString(R.string.all_sets)))
            addTab(this.newTab().setText(getString(R.string.purchased_sets)))

            // tabGravity = TabLayout.GRAVITY_FILL

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        fragmentUbtBinding.ubtVP.currentItem = it
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }

        val root: View = fragmentUbtBinding.ubtTL.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.line_divider_grey))
            drawable.setSize(2, 1)
            (root as LinearLayout).dividerPadding = 10
            (root as LinearLayout).dividerDrawable = drawable
        }
    }

//    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
//        super.setUserVisibleHint(isVisibleToUser)
//        if (isVisibleToUser && firstRun == false) {
//            setupTabLayout()
//            setupViewPager()
//            fragmentUbtBinding.ubtVP.currentItem = 0
//            firstRun = true
//        }
//    }

    private fun navigateToPurchaseTab() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.NavigateToPurchaseTab -> {
                    fragmentUbtBinding.ubtVP.currentItem = 1
                }

                else -> {}
            }
        }
    }

}