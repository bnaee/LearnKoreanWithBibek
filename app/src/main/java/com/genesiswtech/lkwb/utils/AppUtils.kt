package com.genesiswtech.lkwb.utils


import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.data.ErrorResponse
import com.genesiswtech.lkwb.ui.favourite.FavouriteActivity
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.ui.menu.MenuActivity
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.profile.ProfileActivity
import com.genesiswtech.lkwb.ui.setting.SettingActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object AppUtils {

    //menu status bar
    fun statusBarMenu(activity: AppCompatActivity) {
        val window = activity.window;
        window.statusBarColor = ContextCompat.getColor(activity, R.color.menu_bg)
    }

    //invoice status bar
    fun statusBarInvoice(activity: AppCompatActivity) {
        val window = activity.window;
        window.statusBarColor = ContextCompat.getColor(activity, R.color.white)
    }


    //for top window bar, after login
    fun actionbar(activity: AppCompatActivity) {
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_back)
        actionbar.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.gradient_actionbar_background))
        val window = activity.window
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
    }

    //for top window bar, after login
    fun actionBarWithTitle(activity: AppCompatActivity, title: String) {
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_back)
        actionbar.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.gradient_actionbar_background))
        actionbar.title = title

        val window = activity.window
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
    }

    //for top window bar, after login
    fun actionBarWithTitleElevation(activity: AppCompatActivity, title: String) {
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_back)
        actionbar.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.gradient_actionbar_background))
        actionbar.title = title
        actionbar.elevation = 0F
        val window = activity.window
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
    }


    //for top window bar, start page before login, change title header
    fun actionBarStart(activity: AppCompatActivity, title: String) {
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.title = title
        actionbar!!.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_back)
        actionbar.setBackgroundDrawable(activity.resources.getDrawable(R.drawable.gradient_background_same))
        val window = activity.window
        window.setBackgroundDrawableResource(R.drawable.gradient_background)
    }

    //for Home Fragment
    fun actionBarHome(activity: FragmentActivity) {
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionbar.setDisplayShowCustomEnabled(true)
        actionbar.setCustomView(R.layout.layout_main_toolbar)
        val view: View = actionbar.customView
        actionbar.setBackgroundDrawable(activity.resources.getDrawable(R.color.white))
        val window = activity.window
        window.setBackgroundDrawableResource(R.color.white)
    }

    //for Other Tab Fragment
    fun actionBarFragment(activity: FragmentActivity, type: String) {
        val userData = LKWBPreferencesManager.get<LoginDataResponse>("KEY_USER")
        val window = activity.window
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionbar.setDisplayShowCustomEnabled(true)
        actionbar.setDisplayShowTitleEnabled(true)
        actionbar.setCustomView(R.layout.layout_main_toolbar)
        val view: View = actionbar.customView
        val title = view.findViewById<AppCompatTextView>(R.id.actionBarTitleTV)
        val profile = view.findViewById<RelativeLayout>(R.id.actionBarHomeRel)
        val settingMenu = view.findViewById<AppCompatImageButton>(R.id.actionBarSettingIBtn)
        val moreMenu = view.findViewById<AppCompatImageButton>(R.id.actionBarMenuIBtn)
        val notificationMenu = view.findViewById<AppCompatImageButton>(R.id.notificationIBtn)
        val notificationCountMenu = view.findViewById<AppCompatTextView>(R.id.notificationCountTV)

        val profileIV = view.findViewById<CircleImageView>(R.id.actionBarProfileIV)
        val fullNameIV = view.findViewById<AppCompatTextView>(R.id.actionBarFullNameTV)
        val emailTV = view.findViewById<AppCompatTextView>(R.id.actionBarEmailTV)

        fullNameIV.text = userData?.name
        emailTV.text = userData?.email
        Glide.with(activity).load(userData!!.avatar).into(profileIV)
        title.text = type
        if (type == activity.getString(R.string.home)) {
            actionbar.setBackgroundDrawable(activity.getDrawable(R.color.white))
            window.setBackgroundDrawableResource(R.color.white)
            profile.visibility = View.VISIBLE
        } else {
            profile.visibility = View.GONE
            actionbar.setBackgroundDrawable(activity.getDrawable(R.drawable.gradient_actionbar_background))
            window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
        }
        actionbar.setBackgroundDrawable(activity.getDrawable(R.drawable.gradient_actionbar_background))
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)

        settingMenu.setOnClickListener {
            val intent = Intent(activity, FavouriteActivity::class.java)
            activity.startActivity(intent)
        }

        notificationMenu.setOnClickListener {

        }

        moreMenu.setOnClickListener {
            val intent = Intent(activity, MenuActivity::class.java)
            activity.startActivity(intent)
        }

    }

    //for main
    fun actionBarMain(activity: MainActivity, type: String) {
        val userData = LKWBPreferencesManager.get<LoginDataResponse>(LKWBConstants.KEY_USER)
        val userImage = LKWBPreferencesManager.getString(LKWBConstants.USER_IMAGE)
        val userName = LKWBPreferencesManager.getString(LKWBConstants.USER_NAME)
        val window = activity.window
        val actionbar = (activity as AppCompatActivity).supportActionBar
        actionbar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionbar.setDisplayShowCustomEnabled(true)
        actionbar.setDisplayShowTitleEnabled(true)
        actionbar.setCustomView(R.layout.layout_main_toolbar)
        val view: View = actionbar.customView
        val title = view.findViewById<AppCompatTextView>(R.id.actionBarTitleTV)
        val profile = view.findViewById<RelativeLayout>(R.id.actionBarHomeRel)
        val settingMenu = view.findViewById<AppCompatImageButton>(R.id.actionBarSettingIBtn)
        val moreMenu = view.findViewById<AppCompatImageButton>(R.id.actionBarMenuIBtn)
        val notificationMenu = view.findViewById<AppCompatImageButton>(R.id.notificationIBtn)
        val notificationCountMenu = view.findViewById<AppCompatTextView>(R.id.notificationCountTV)
        val actionBarName = view.findViewById<LinearLayout>(R.id.actionBarName)
        val profileIV = view.findViewById<CircleImageView>(R.id.actionBarProfileIV)
        val fullNameIV = view.findViewById<AppCompatTextView>(R.id.actionBarFullNameTV)
        val emailTV = view.findViewById<AppCompatTextView>(R.id.actionBarEmailTV)
        notificationCountMenu.visibility = View.VISIBLE
        fullNameIV.text = userName
        emailTV.text = userData?.email
        Glide.with(activity).load(userImage).error(R.drawable.ic_register).into(profileIV)

        title.text = type

        if (type == activity.getString(R.string.home)) {
            actionBarName.visibility = View.GONE
//            actionbar.setBackgroundDrawable(activity.getDrawable(R.color.white))
//            window.setBackgroundDrawableResource(R.color.white)
            profile.visibility = View.VISIBLE
        } else {
            actionBarName.visibility = View.GONE
            profile.visibility = View.VISIBLE
//            actionbar.setBackgroundDrawable(activity.getDrawable(R.drawable.gradient_actionbar_background))
//            window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)
        }
        actionbar.setBackgroundDrawable(activity.getDrawable(R.drawable.gradient_actionbar_background))
        window.setBackgroundDrawableResource(R.drawable.gradient_actionbar_background)

        title.setOnClickListener {
            if (LKWBPreferencesManager.readLanguageCode() == "en") {
                LKWBPreferencesManager.writeLanguageCode("ko")
            } else {
                LKWBPreferencesManager.writeLanguageCode("en")
            }
            (activity as? MainActivity)?.recreate()
        }
        profile.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            activity.startActivity(intent)
        }

        settingMenu.setOnClickListener {
            val intent = Intent(activity, ProfileActivity::class.java)
            activity.startActivity(intent)
        }

        notificationMenu.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            activity.startActivity(intent)
        }

        moreMenu.setOnClickListener {
            val intent = Intent(activity, MenuActivity::class.java)
            activity.startActivity(intent)
        }


    }

    //for top window bar only
    fun windowBackground(activity: AppCompatActivity) {
        val window = activity.window
        window.setBackgroundDrawableResource(R.drawable.gradient_background)
    }

    fun RecyclerView.addItemDecorationWithoutLastItem() {
        if (layoutManager !is LinearLayoutManager) return
        addItemDecoration(DividerItemDecorator(context))
    }

    fun RecyclerView.addItemDecorationWithoutLastItemGrey() {
        if (layoutManager !is LinearLayoutManager) return
        addItemDecoration(DividerItemDecoratorGrey(context))
    }

    val EMAIL_ADDRESS_PATTERN = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
    )

    fun isValidEmail(str: String): Boolean {
        return EMAIL_ADDRESS_PATTERN.matcher(str).matches()
    }


    fun showSnackBar(message: String?, activity: Activity?) {
        var mes: String? = activity!!.getString(R.string.request_failed)
        if (message == null) mes = activity.getString(R.string.request_failed)
        else mes = message
        val snackBar = Snackbar.make(
            activity!!.findViewById(android.R.id.content), mes, Snackbar.LENGTH_SHORT
        ).setAction("") {
//                on click btn
        }.setActionTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
        val snackBarView = snackBar.view
        val txt =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        snackBarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
        txt.setTextColor(ContextCompat.getColor(activity, R.color.white))
        snackBar.show()
    }

    fun getErrorMessage(errorMessage: ResponseBody?): String {
        var error: String? = null
        val gson = Gson()
        val type = object : TypeToken<ErrorResponse>() {}.type
        val errorResponse: ErrorResponse? = gson.fromJson(errorMessage!!.charStream(), type)
        error = errorResponse!!.message
        return error.toString()
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context): File {
        var mCurrentPhotoPath: String? = null
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }

    fun showDialog(
        context: Context,
        title: String,
        subTitle: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog)
        val dialogTitle = dialog.findViewById(R.id.titleTV) as AppCompatTextView
        val dialogSubTitle = dialog.findViewById(R.id.subTitleTV) as AppCompatTextView
        val dialogPositiveButton = dialog.findViewById(R.id.yesBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        dialogTitle.text = title
        dialogSubTitle.text = subTitle
        dialogPositiveButton.text = titleOfPositiveButton
        dialogNegativeButton.text = titleOfNegativeButton
        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showSingleDialog(
        context: Context,
        title: String,
        titleOfPositiveButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_single)
        val dialogTitle = dialog.findViewById(R.id.titleTV) as AppCompatTextView
        val dialogPositiveButton = dialog.findViewById(R.id.yesBtn) as AppCompatButton
        dialogTitle.text = title
        dialogPositiveButton.text = titleOfPositiveButton
        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showPurchaseDialog(
        context: Context,
        title: String,
        subTitle: String,
        titleOfPositiveButton: String? = null,
        positiveButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_dialog_purchase)
        val dialogTitle = dialog.findViewById(R.id.titleTV) as AppCompatTextView
        val dialogSubTitle = dialog.findViewById(R.id.subTitleTV) as AppCompatTextView
        val dialogPositiveButton = dialog.findViewById(R.id.yesBtn) as AppCompatButton
        dialogTitle.text = title
        dialogSubTitle.text = subTitle
        dialogPositiveButton.text = titleOfPositiveButton
        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun shareText(activity: Activity, title: String, subject: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_SUBJECT, title)
        share.putExtra(Intent.EXTRA_TEXT, subject)
        activity.startActivity(Intent.createChooser(share, activity.getString(R.string.share_via)))
    }

    fun shareTextImage(activity: Activity, title: String, subject: String, bitmap: Bitmap) {
        val uri = getImageToShare(activity, bitmap)
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.putExtra(Intent.EXTRA_TEXT, title)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.type = "image/png"
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_via)))
    }

    // Retrieving the url to share
    private fun getImageToShare(activity: Activity, bitmap: Bitmap): Uri? {
        val imagefolder = File(activity.cacheDir, "my_images")
        var uri: Uri? = null
        try {
            imagefolder.mkdirs()
            val file = File(imagefolder, "shared_image.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, outputStream)
            outputStream.flush()
            outputStream.close()
            uri = FileProvider.getUriForFile(activity, "com.genesiswtech.lkwb.fileprovider", file)
        } catch (e: Exception) {

            Log.d("TAG", "Share Error: " + e.message)
        }
        return uri
    }

    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    fun checkAndRequestPermissions(context: Activity?): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(
            context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded.toTypedArray(), REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    fun saveProfileData(loginResponse: LoginDataResponse) {
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_ID, loginResponse!!.id.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_EMAIL, loginResponse!!.email.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_IMAGE, loginResponse.avatar.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_NAME, loginResponse.name.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_REWARD_POINT, loginResponse.rewardPoint.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_REF_CODE, loginResponse.refCode.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.VERIFIED, loginResponse.emailVerification.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.PROVIDER, loginResponse.provider.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.IS_NEW_USER, loginResponse.isNewUser.toString()
        )
    }

    fun logOutCall(activity: Activity) {
        LKWBPreferencesManager.put(null, LKWBConstants.KEY_USER)
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        activity.finish()
        activity.startActivity(intent)
    }

    fun showAddDiscussionDialog(
        context: Context,
        positiveButtonFunction: (() -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_discussion)
        val dialogTitle = dialog.findViewById(R.id.titleEdt) as AppCompatEditText
        val dialogSubTitle = dialog.findViewById(R.id.descriptionEdt) as AppCompatEditText
        val dialogPositiveButton = dialog.findViewById(R.id.addDiscussionBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showImageDialog(
        context: Context, imageUrl: String
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_image)
        val dialogImage = dialog.findViewById(R.id.image) as? AppCompatImageView
        val dialogCancelButton = dialog.findViewById(R.id.closeIBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }
        Glide.with(context).load(imageUrl).into(dialogImage!!)

        dialog.show()
    }

    fun showBannerImageDialog(
        context: Context, imageUrl: String
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_banner_image)
        val dialogImage = dialog.findViewById(R.id.image) as? AppCompatImageView
        val dialogCancelButton = dialog.findViewById(R.id.closeIBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }
        Glide.with(context).load(imageUrl).into(dialogImage!!)

        dialog.show()
    }


    fun hideKeyboard(activity: AppCompatActivity) {
        try {
            val imm: InputMethodManager? =
                activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        } catch (e: java.lang.Exception) {
            // TODO: handle exception
        }
    }


//    private fun editCommentDialog(data: CommentDataResponse) {
//        val dialog = Dialog(this, R.style.Theme_Dialog)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_comment_edit)
//        val commentTV = dialog.findViewById(R.id.fullNameEdt) as AppCompatEditText
//        dialogTitle.setText(data.body.toString())
//        val dialogPositiveButton = dialog.findViewById(R.id.updateBtn) as AppCompatButton
//        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
//        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton
//
//        dialogCancelButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialogPositiveButton.setOnClickListener {
//            AppUtils.hideKeyboard(this)
//            if (dialogTitle.text.isNullOrEmpty()) showSnackBar(getString(R.string.enter_comment))
//            else {
//                discussionDetailPresenter!!.postEditComment(
//                    this,
//                    data.id!!.toInt(),
//                    "PUT",
//                    dialogTitle.text.toString()
//                )
//                dialog.dismiss()
//            }
//
//        }
//        dialogNegativeButton.setOnClickListener {
//            dialog.dismiss()
//        }
//        dialog.show()
//    }

    fun showCommentDialog(
        context: Context,
        type: String,
        title: String,
        comment: String,
        titleOfPositiveButton: String? = null,
        titleOfNegativeButton: String? = null,
        positiveButtonFunction: ((String) -> Unit)? = null,
        negativeButtonFunction: (() -> Unit)? = null
    ) {
        val dialog = Dialog(context, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_comment_edit)
        val dialogTitle = dialog.findViewById(R.id.dialogTitleTV) as AppCompatTextView
        val commentEdt = dialog.findViewById(R.id.fullNameEdt) as AppCompatEditText
        val dialogPositiveButton = dialog.findViewById(R.id.updateBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        dialogTitle.text = title
        commentEdt.setText(comment)
        dialogPositiveButton.text = titleOfPositiveButton
        dialogNegativeButton.text = titleOfNegativeButton
        dialogPositiveButton.setOnClickListener {
            positiveButtonFunction?.invoke(commentEdt.text.toString())
            dialog.dismiss()
        }
        dialogNegativeButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }

        dialogCancelButton.setOnClickListener {
            negativeButtonFunction?.invoke()
            dialog.dismiss()
        }
        dialog.show()
    }


    fun setAppLocale(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    fun centerTitle(activity: AppCompatActivity) {
        val textViews = ArrayList<View>()
        activity.window.decorView.findViewsWithText(
            textViews,
            activity.title,
            View.FIND_VIEWS_WITH_TEXT
        )
        if (textViews.size > 0) {
            var appCompatTextView: AppCompatTextView? = null
            if (textViews.size == 1)
                appCompatTextView = textViews[0] as AppCompatTextView
            else {
                for (v in textViews) {
                    if (v.parent is Toolbar) {
                        appCompatTextView = v as AppCompatTextView
                        break
                    }
                }
            }
            if (appCompatTextView != null) {
                val params = appCompatTextView.layoutParams
                params.width = ViewGroup.LayoutParams.MATCH_PARENT
                appCompatTextView.layoutParams = params
                appCompatTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
            }
        }
    }

    enum class Device {
        DEVICE_TYPE
    }

    fun isTab(context: Context, device: Device): Boolean? {
        try {
            if (device == Device.DEVICE_TYPE) {
                return if (isTablet(context)) {
                    getDeviceInch(context)
                } else {
                    false
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getDeviceInch(context: Context): Boolean {
        return try {
            val displayMetrics = context.resources.displayMetrics
            val yinch = displayMetrics.heightPixels / displayMetrics.ydpi
            val xinch = displayMetrics.widthPixels / displayMetrics.xdpi
            val diagonalinch = Math.sqrt((xinch * xinch + yinch * yinch).toDouble())
            diagonalinch >= 7
        } catch (e: java.lang.Exception) {
            false
        }
    }

    fun isTablet(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    fun isDeviceTab(activity: AppCompatActivity): Boolean {
        if (isTab(
                activity,
                Device.DEVICE_TYPE
            ) == false || activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        )
            return true
        return false
    }

    fun isDeviceMobileLandScape(activity: AppCompatActivity): Boolean {
//        if (isTab(
//                activity,
//                Device.DEVICE_TYPE
//            ) == false && activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
//        )
        if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true
        return false
    }


}

