package com.genesiswtech.lkwb.ui.changeLanguage

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityChangeLanguageBinding
import com.genesiswtech.lkwb.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class ChangeLanguageActivity : BaseActivity<ActivityChangeLanguageBinding>() {

    private lateinit var changeLanguageBinding: ActivityChangeLanguageBinding

    private val lkwbEventBus by inject<LKWBEventBus>()
    var languageAdapter: ChangeLanguageAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        setTitle(R.string.change_language)
        changeLanguageBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_language)
        changeLanguageBinding.changeLanguageHandler = this
        val data = ArrayList<String>()
        data.add(getString(R.string.english))
        data.add(getString(R.string.korean))
        setChangeLanguageAdapter(data)

    }

    private fun setChangeLanguageAdapter(data: ArrayList<String>) {
        changeLanguageBinding.changeLanguageRV.layoutManager = LinearLayoutManager(this)
        languageAdapter = ChangeLanguageAdapter(data)
        changeLanguageBinding.changeLanguageRV.adapter = languageAdapter
        changeLanguageBinding.changeLanguageRV.addItemDecorationWithoutLastItem()
        languageAdapter!!.onItemClick = {
            if (it == getString(R.string.english)) {
                if (LKWBPreferencesManager.readLanguageCode() == "ko") {
                    showDialog(it)
                }
            } else if (it == getString(R.string.korean)) {
                if (LKWBPreferencesManager.readLanguageCode() == "en") {
                    showDialog(it)
                }
            }
        }
    }

    private fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager)
            return
        addItemDecoration(DividerItemDecoratorGrey(context))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun changeLanguage() {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.ChangeLanguage) }
    }

    override val binding get() = ActivityChangeLanguageBinding.inflate(layoutInflater)

    private fun showDialog(type: String) {
        var text = getString(R.string.change_app_language_english)
        if (type == getString(R.string.korean))
            text = getString(R.string.change_app_language_korean)
        AppUtils.showDialog(this,
            title = getString(R.string.change_language),
            text,
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.no),
            positiveButtonFunction = {
                if (type == getString(R.string.english)) {
                    if (LKWBPreferencesManager.readLanguageCode() == "ko") {
                        LKWBPreferencesManager.writeLanguageCode("en")
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }
                } else if (type == getString(R.string.korean)) {
                    if (LKWBPreferencesManager.readLanguageCode() == "en") {
                        LKWBPreferencesManager.writeLanguageCode("ko")
                        startActivity(intent)
                        finish()
                        overridePendingTransition(0, 0)
                    }
                }
                changeLanguage()
                languageAdapter!!.notifyDataSetChanged()
            },
            negativeButtonFunction = { }
        )
    }


}