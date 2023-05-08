package com.genesiswtech.lkwb.utils

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R

class FullImageActivity : AppCompatActivity() {

    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
// Hide status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        url = intent.getStringExtra(LKWBConstants.BLOG_RESULT)
        setContentView(R.layout.layout_question_image)
        val close: AppCompatImageButton = findViewById(R.id.closeIBtn)
        close.setOnClickListener {
            finish()
        }
        val image: CustomZoom = findViewById(R.id.image)
        Glide.with(this)
            .load(url)
            .into(image)
    }
}