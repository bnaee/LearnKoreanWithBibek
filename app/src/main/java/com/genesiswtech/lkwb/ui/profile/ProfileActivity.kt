package com.genesiswtech.lkwb.ui.profile


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.otpVerification.OTPVerificationActivity
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileDataResponse
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileResponse
import com.genesiswtech.lkwb.ui.profile.presenter.ProfilePresenter
import com.genesiswtech.lkwb.ui.profile.view.IProfileView
import com.genesiswtech.lkwb.utils.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File


class ProfileActivity : BaseActivity<ActivityProfileBinding>(), IProfileView {

    private lateinit var profileBinding: ActivityProfileBinding
    private var profilePresenter: ProfilePresenter? = null
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    var photoFile: File? = null
    var mCurrentPhotoPath: String? = null

    private val lkwbEventBus by inject<LKWBEventBus>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.actionbar(this)
        setTitle(R.string.profile)
        profileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        profileBinding.settinghandler = this
        initDependencies()
        setData()
        profilePresenter!!.getProfile(this)
    }

    private fun setData() {
        profileBinding.fullNameTV.text = LKWBPreferencesManager.getString(LKWBConstants.USER_NAME)
        profileBinding.emailTV.text = LKWBPreferencesManager.getString(LKWBConstants.USER_EMAIL)
        profileBinding.nameTV.text = LKWBPreferencesManager.getString(LKWBConstants.USER_NAME)
        profileBinding.refCodeTV.text =
            LKWBPreferencesManager.getString(LKWBConstants.USER_REF_CODE)
        setProfilePic(LKWBPreferencesManager.getString(LKWBConstants.USER_IMAGE).toString())

        if (LKWBPreferencesManager.getString(LKWBConstants.PROVIDER) != LKWBConstants.EMAIL)
            profileBinding.editPasswordTV.visibility = View.GONE
    }

    override fun initDependencies() {
        profilePresenter = ProfilePresenter(this, application)
    }

    override fun onSettingCLClick(v: View?) {
        if (AppUtils.checkAndRequestPermissions(this)) {
            chooseImage(this)
        }
    }

    override fun onEditFullNameClick(v: View?) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_profile_edit)
        val dialogTitle = dialog.findViewById(R.id.fullNameEdt) as AppCompatEditText
        dialogTitle.setText(LKWBPreferencesManager.getString(LKWBConstants.USER_NAME))
        val dialogPositiveButton = dialog.findViewById(R.id.updateBtn) as AppCompatButton
        val dialogNegativeButton = dialog.findViewById(R.id.cancelBtn) as AppCompatButton
        val dialogCancelButton = dialog.findViewById(R.id.closeBtn) as AppCompatImageButton

        dialogCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogPositiveButton.setOnClickListener {
            AppUtils.hideKeyboard(this)
            if (dialogTitle.text.isNullOrEmpty()) showSnackBar(getString(R.string.enter_full_name))
            else {
                profilePresenter!!.putUpdateFullName(this, dialogTitle.text.toString())
                dialog.dismiss()
            }

        }
        dialogNegativeButton.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    override fun onEditPasswordClick(v: View?) {
        showChangePasswordDialog()
    }

    override fun onDeleteAccountClick(v: View?) {
        showAccountDeleteDialog()
    }

    override fun onLogOutClick(v: View?) {
        showLogoutDialog()
    }

    override fun onSuccess(profileData: LoginDataResponse?) {
        AppUtils.saveProfileData(profileData!!)
        setData()
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateImageFromSetting) }
    }

    override fun onLogOutSuccess(logOutResponse: LogOutResponse) {
        AppUtils.logOutCall(this)
    }

    override fun onFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onTimeOut() {
        showSnackBar(getString(R.string.time_out))
    }

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message, this)
    }

    override fun onShowProgressBar(status: Boolean) {
        profileBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun onProfileUpdateSuccess(updateProfileDataResponse: UpdateProfileResponse) {
        profileBinding.fullNameTV.text = updateProfileDataResponse.data!!.name
        profileBinding.nameTV.text = updateProfileDataResponse.data!!.name
        updateUserData(updateProfileDataResponse.data!!)
        setProfilePic(updateProfileDataResponse.data!!.image.toString())
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun uploadPhoto(picturePath: String) {
        profilePresenter!!.putUpdateProfilePicture(
            this, picturePath
        )
    }

    private fun chooseImage(context: Context) {
        val optionsMenu = resources.getStringArray(R.array.upload_option)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setItems(
            optionsMenu
        ) { dialogInterface, i ->
            if (optionsMenu[i] == getString(R.string.take_photo)) {
                // Open the camera and get the photo
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                // Create the File where the photo should go
                try {
                    photoFile = AppUtils.createImageFile(context)
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCurrentPhotoPath = photoFile!!.absolutePath
                        val photoURI = FileProvider.getUriForFile(
                            this, "com.genesiswtech.lkwb.fileprovider", photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, 0)
                    }
                } catch (e: Exception) {
                    // Error occurred while creating the File
                    Log.d("TAG", "Error File: " + e.message)
                }


            } else if (optionsMenu[i] == getString(R.string.choose_gallery)) {
                // choose from  external storage
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(pickPhoto, 1)
            } else if (optionsMenu[i] == getString(R.string.exit)) {
                dialogInterface.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && photoFile != null) {
                    val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
//                    settingBinding.userIV.setImageBitmap(myBitmap)
                    uploadPhoto(mCurrentPhotoPath.toString())
                }
                1 -> if (resultCode == RESULT_OK && data != null) {
                    val selectedImage: Uri? = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    if (selectedImage != null) {
                        val cursor: Cursor? =
                            contentResolver.query(selectedImage, filePathColumn, null, null, null)
                        if (cursor != null) {
                            cursor.moveToFirst()
                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                            val picturePath: String = cursor.getString(columnIndex)
//                            settingBinding.userIV.setImageBitmap(
//                                BitmapFactory.decodeFile(
//                                    picturePath
//                            )
//                            )
                            cursor.close()
                            uploadPhoto(picturePath)

                        }
                    }
                }
            }
        }
    }

    private fun updateUserData(updateProfileData: UpdateProfileDataResponse) {
        LKWBPreferencesManager.putString(LKWBConstants.USER_NAME, updateProfileData.name.toString())
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_IMAGE, updateProfileData.image.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_REWARD_POINT, updateProfileData.rewardPoint.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_REF_CODE, updateProfileData.refCode.toString()
        )
        LKWBPreferencesManager.putString(
            LKWBConstants.USER_EMAIL, updateProfileData.email.toString()
        )
        lifecycleScope.launch { lkwbEventBus.emit(LKWBEvents.UpdateImageFromSetting) }
    }

    private fun setProfilePic(url: String) {
        Glide.with(this).load(url).placeholder(R.drawable.ic_register).into(profileBinding.userIV)
    }

    private fun showLogoutDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.logout),
            getString(R.string.want_to_logout),
            titleOfPositiveButton = getString(R.string.logout),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                profilePresenter!!.postLogOut(this)
            },
            negativeButtonFunction = { }
        )
    }

    private fun showChangePasswordDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.change_password_),
            getString(R.string.want_to_change_password),
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                val intent = Intent(this, OTPVerificationActivity::class.java)
                intent.putExtra(LKWBConstants.EMAIL, profileBinding.emailTV.text)
                intent.putExtra(LKWBConstants.PROFILE, true)
                startActivity(intent)
            },
            negativeButtonFunction = { }
        )
    }

    private fun showAccountDeleteDialog() {
        AppUtils.showDialog(this,
            title = getString(R.string.delete_account),
            getString(R.string.want_to_delete_account),
            titleOfPositiveButton = getString(R.string.yes),
            titleOfNegativeButton = getString(R.string.cancel),
            positiveButtonFunction = {
                val intent = Intent(this, OTPVerificationActivity::class.java)
                intent.putExtra(LKWBConstants.EMAIL, profileBinding.emailTV.text)
                intent.putExtra(LKWBConstants.DELETE, true)
                startActivity(intent)
            },
            negativeButtonFunction = { }
        )
    }

    override val binding: ActivityProfileBinding
        get() = ActivityProfileBinding.inflate(layoutInflater)

}