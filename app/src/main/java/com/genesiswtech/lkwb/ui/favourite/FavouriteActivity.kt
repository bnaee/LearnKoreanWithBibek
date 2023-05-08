package com.genesiswtech.lkwb.ui.favourite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityFavouriteBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.favourite.view.IFavouriteView
import com.genesiswtech.lkwb.ui.favouriteDetail.FavouriteDetailActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.DividerItemDecoratorGrey
import com.genesiswtech.lkwb.utils.LKWBConstants

class FavouriteActivity : BaseActivity<ActivityFavouriteBinding>(), IFavouriteView {

    private lateinit var favouriteBinding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        setTitle(R.string.favourite)
        favouriteBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_favourite)
        favouriteBinding.favouriteHandler = this

        val data = ArrayList<String>()
        data.add(getString(R.string.dictionarys))
        data.add(getString(R.string.blogs))
        data.add(getString(R.string.grammars))
        setFavouriteAdapter(data)

    }

    private fun setFavouriteAdapter(data: ArrayList<String>) {
        favouriteBinding.favouriteRV.layoutManager = LinearLayoutManager(this)
        val favouriteAdapter = FavouriteAdapter(data)
        favouriteBinding.favouriteRV.adapter = favouriteAdapter
        favouriteBinding.favouriteRV.addItemDecorationWithoutLastItem()
        favouriteAdapter.onItemClick = {
            val intent = Intent(this, FavouriteDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_TITLE, it)
            startActivity(intent)
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

    override val binding: ActivityFavouriteBinding
        get() = ActivityFavouriteBinding.inflate(layoutInflater)
}