package com.genesiswtech.lkwb.ui.blogDetail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.databinding.ActivityBlogDetailBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.BlogDetailDataResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.LatestBlogs
import com.genesiswtech.lkwb.ui.blogDetail.presenter.BlogDetailPresenter
import com.genesiswtech.lkwb.ui.blogDetail.view.IBlogDetailView
import com.genesiswtech.lkwb.ui.comment.CommentActivity
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.AppUtils.addItemDecorationWithoutLastItemGrey
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBEventBus
import com.genesiswtech.lkwb.utils.LKWBEvents
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class BlogDetailActivity : BaseActivity<ActivityBlogDetailBinding>(), IBlogDetailView {

    private lateinit var blogDetailBinding: ActivityBlogDetailBinding
    private var blogData: BlogDetailDataResponse? = null
    private var blogDetailPresenter: BlogDetailPresenter? = null
    private var title: String? = null
    private var id: Int? = null
    private var bitmap: Bitmap? = null
    private var isFavourite: Boolean? = null
    lateinit var adRequest: AdRequest

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        id = intent.getIntExtra(LKWBConstants.DATA_ID, 0)
        title = intent.getStringExtra(LKWBConstants.DATA_TITLE)

        AppUtils.actionBarWithTitle(this, title!!)
        blogDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_blog_detail)
        blogDetailBinding.blogDetailHandler = this

        initDependencies()
        initializeAd()
        blogDetailPresenter!!.getSingleBlog(this, id!!)
        removeFavouriteFromList()

    }

    private fun initializeAd() {
        MobileAds.initialize(this)
        adRequest = AdRequest.Builder().build()
        blogDetailBinding.adView.loadAd(adRequest)
    }


    private fun setData(blogData: BlogDetailDataResponse) {
        isFavourite = blogData.isFavorite
        blogDetailBinding.blogTitleTV.text = blogData.title
        blogDetailBinding.blogTimeTV.text = blogData.publishedAt
        if (blogData.total_comments!!.toInt() == 0)
            blogDetailBinding.blogCommentCountTV.text = getString(R.string.comment)
        if (blogData.total_comments!!.toInt() > 1)
            blogDetailBinding.blogCommentCountTV.text =
                blogData.total_comments.toString() + " " + getString(R.string.comments)
        else if (blogData.total_comments!!.toInt() == 1)
            blogDetailBinding.blogCommentCountTV.text =
                blogData.total_comments.toString() + " " + getString(R.string.comment)
        blogDetailBinding.blogDescriptionTV.settings.domStorageEnabled = true
        blogDetailBinding.blogDescriptionTV.settings.setAppCacheEnabled(true)
        blogDetailBinding.blogDescriptionTV.settings.loadsImagesAutomatically = true
        blogDetailBinding.blogDescriptionTV.settings.mixedContentMode =
            WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        blogDetailBinding.blogDescriptionTV.loadDataWithBaseURL(
            null,
            blogData.description.toString(),
            "text/html",
            "UTF-8",
            null
        )
        if (isFavourite == true)
            blogDetailBinding.blogFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))

        Glide.with(this)
            .asBitmap()
            .load(blogData.image)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    blogDetailBinding.blogIV.setImageBitmap(resource)
                    bitmap = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // this is called when imageView is cleared on lifecycle call or for
                    // some other reason.
                    // if you are referencing the bitmap somewhere else too other than this imageView
                    // clear it here as you can no longer have the bitmap
                }
            })
    }


    private fun setBlogAdapter(data: ArrayList<LatestBlogs>) {
        blogDetailBinding.recyclerViewBlog.layoutManager = LinearLayoutManager(this)
        val blogAdapter = BlogDetailAdapter(data, this)
        blogDetailBinding.recyclerViewBlog.adapter = blogAdapter
        blogDetailBinding.recyclerViewBlog.addItemDecorationWithoutLastItemGrey()
        blogAdapter.onItemClick = {
            val intent = Intent(this, BlogDetailActivity::class.java)
            intent.putExtra(LKWBConstants.DATA_ID, it.id)
            intent.putExtra(LKWBConstants.DATA_TITLE, it.title)
            startActivity(intent)
        }
    }

    override fun onBlogShareClick(v: View?) {
        AppUtils.shareText(this, title!!, blogData!!.shareLink.toString())
    }

    override fun onBlogFavouriteClick(v: View?) {
        if (isFavourite == true)
            blogDetailPresenter!!.postRemoveFavouriteBlog(this, id!!, LKWBConstants.BLOG)
        else
            blogDetailPresenter!!.postAddFavouriteBlog(this, id!!, LKWBConstants.BLOG)
    }

    override fun onCommentCountClick(v: View?) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra(LKWBConstants.DATA_ID, id)
        startActivity(intent)
    }

    override fun initDependencies() {
        blogDetailPresenter = BlogDetailPresenter(this, application)
    }

    override fun onSuccess(blogDetailDataResponse: BlogDetailDataResponse) {
        blogData = blogDetailDataResponse
        setData(blogDetailDataResponse)
        setBlogAdapter(blogDetailDataResponse.latestBlogs)
        blogDetailBinding.blogDetailLinear.visibility = View.VISIBLE

    }

    override fun onAddFavouriteSuccess(blogFavouriteResponse: FavouriteResponse) {
        if (blogFavouriteResponse.code == 200) {
            isFavourite = true
            blogDetailBinding.blogFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite_click))
            showSnackBar(getString(R.string.add_to_favourite))
            blogFavouriteResponse.data?.let { addFavouritesFromList(it) }
        }
    }

    override fun onRemoveFavouriteSuccess(blogFavouriteResponse: FavouriteResponse) {
        if (blogFavouriteResponse.code == 200) {
            isFavourite = false
            blogDetailBinding.blogFavouriteIBtn.setImageDrawable(getDrawable(R.drawable.ic_favourite))
            showSnackBar(getString(R.string.remove_from_favourite))
            removeFavouritesFromList(id!!)
        }
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        blogDetailBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun removeFavouritesFromList(id: Int) {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.RemoveFavourites(id)) }
    }

    private fun addFavouritesFromList(blogDataResponse: BlogDataResponse) {
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.AddBlogFavourites(blogDataResponse)) }
    }

    private fun removeFavouriteFromList() {
        lkwbEventBus.subscribe(lifecycleScope) { lkwbEvent ->
            when (lkwbEvent) {
                is LKWBEvents.UpdateBlogFromComment -> {
                    blogDetailPresenter!!.getSingleBlog(this, id!!)
                }
                else -> {}
            }
        }
    }

    override val binding: ActivityBlogDetailBinding
        get() = ActivityBlogDetailBinding.inflate(layoutInflater)
}