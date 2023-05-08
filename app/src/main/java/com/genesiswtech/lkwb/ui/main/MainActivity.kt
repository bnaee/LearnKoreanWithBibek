package com.genesiswtech.lkwb.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityMainBinding
import com.genesiswtech.lkwb.ui.dictionary.DictionaryFragment
import com.genesiswtech.lkwb.ui.discussion.DiscussionFragment
import com.genesiswtech.lkwb.ui.grammar.GrammarFragment
import com.genesiswtech.lkwb.ui.home.HomeFragment
import com.genesiswtech.lkwb.ui.home.view.IHomeDataPass
import com.genesiswtech.lkwb.ui.main.view.IMainView
import com.genesiswtech.lkwb.ui.menu.MenuActivity
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.setting.SettingActivity
import com.genesiswtech.lkwb.ui.ubt.UBTFragment
import com.genesiswtech.lkwb.utils.*
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>(), IMainView, IHomeDataPass {

    private lateinit var mainBinding: ActivityMainBinding
    var prevMenuItem: MenuItem? = null
    private val lkwbEventBus by inject<LKWBEventBus>()
    var language: Boolean = false
    private var mInterstitialAd: InterstitialAd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.mainhandler = this
        setActionbar()
        setupViewPager()
        setBottomNavigation()
        updateToolbarFromSetting()
        loadInterstitialAd()

        if (intent.getIntExtra(LKWBConstants.DATA_TYPE, 0) != 0) {
//            mainBinding.bottomNavigationView.menu.getItem(
//                intent.getIntExtra(
//                    LKWBConstants.DATA_TYPE,
//                    0
//                )
//            ).isChecked = true
            mainBinding.mainVP.currentItem = intent.getIntExtra(LKWBConstants.DATA_TYPE, 0)
        }


    }

    private fun loadInterstitialAd() {
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.banner_inter_id),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG Ad", adError!!.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d("TAG Ad", "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@MainActivity)
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.")
                    }
                }
            })
    }

    private fun setupViewPager() {
        setDashboardTitle()
        val adapter = MainPagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment())
        adapter.addFragment(UBTFragment())
        adapter.addFragment(GrammarFragment())
        adapter.addFragment(DictionaryFragment())
        adapter.addFragment(DiscussionFragment())
        mainBinding.mainVP.adapter = adapter
        mainBinding.mainVP.offscreenPageLimit = 4
        mainBinding.mainVP.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem!!.isChecked = false
                } else {
                    mainBinding.bottomNavigationView.menu.getItem(0).isChecked = false
                }
                mainBinding.bottomNavigationView.menu.getItem(position).isChecked = true
                prevMenuItem = mainBinding.bottomNavigationView.menu.getItem(position)
                if (mainBinding.bottomNavigationView.menu.getItem(0).isChecked) {
                    setDashboardTitle()
                } else
                    mainBinding.actionBarTitleTV.text =
                        mainBinding.bottomNavigationView.menu.getItem(position).title.toString()
//                AppUtils.actionBarMain(
//                    this@MainActivity,
//                    mainBinding.bottomNavigationView.menu.getItem(position).title.toString()
//                )
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    fun firstWord(input: String): String? {
        return input.split(" ").toTypedArray()[0] // Create array of words and return the 0th word
    }

    private fun setBottomNavigation() {
        mainBinding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    mainBinding.mainVP.currentItem = 0
                    setDashboardTitle()
                }
                R.id.ubtTestFragment -> mainBinding.mainVP.currentItem = 1
                R.id.grammarFragment -> mainBinding.mainVP.currentItem = 2
                R.id.dictionaryFragment -> mainBinding.mainVP.currentItem = 3
                R.id.discussionFragment -> mainBinding.mainVP.currentItem = 4
            }
            false
        }
    }

    private fun setDashboardTitle() {
        val first = firstWord(
            LKWBPreferencesManager.getString(LKWBConstants.USER_NAME).toString()
        )
        mainBinding.actionBarTitleTV.text = first + "'s " + getString(R.string.home)
    }

    override fun onResume() {
        super.onResume()
        languageRestart()
        val b: Singleton? = Singleton.instance
        Log.d("TAG", b!!.getData().toString())
        if (b.getData().toString() == getString(R.string.home))
            mainBinding.bottomNavigationView.selectedItemId = R.id.homeFragment
        if (b.getData().toString() == getString(R.string.blog)) {
            mainBinding.bottomNavigationView.selectedItemId = R.id.blogFragment
        }
        val a: Singleton? = Singleton.instance
        a!!.setData(null)
    }

    override fun onHomeDataPass(data: String) {
        if (data.contains(getString(R.string.ubt_test)))
            mainBinding.mainVP.currentItem = 1
        if (data.contains(getString(R.string.grammar)))
            mainBinding.mainVP.currentItem = 2
        if (data.contains(getString(R.string.dictionary)))
            mainBinding.mainVP.currentItem = 3
        if (data.contains(getString(R.string.discussion)))
            mainBinding.mainVP.currentItem = 4
    }

    private fun updateToolbarFromSetting() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateImageFromSetting -> {
                    setActionbar()
                }
                is LKWBEvents.NavigateToPurchaseTab -> {
                    mainBinding.mainVP.currentItem = 1
                }
                is LKWBEvents.ChangeLanguage -> {
                    language = true
                }
                else -> {}
            }
        }
    }

    private fun setActionbar() {
        Glide.with(this).load(LKWBPreferencesManager.getString(LKWBConstants.USER_IMAGE))
            .error(R.drawable.ic_register)
            .into(mainBinding.actionBarProfileIV)
    }

    override val binding: ActivityMainBinding
        get() = ActivityMainBinding.inflate(layoutInflater)

    override fun onProfileClick(v: View?) {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun onTitleClick(v: View?) {
    }

    override fun onNotificationClick(v: View?) {
        val intent = Intent(this, NotificationActivity::class.java)
        startActivity(intent)
    }

    override fun onMenuClick(v: View?) {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    private fun languageRestart() {
        if (language) {
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
            language = false
        }
    }

}