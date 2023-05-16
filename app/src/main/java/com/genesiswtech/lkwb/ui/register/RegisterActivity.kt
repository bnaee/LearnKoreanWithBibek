package com.genesiswtech.lkwb.ui.register

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.FacebookPictureResponse
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivityRegisterBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.loginReward.LoginRewardActivity
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.ui.otpVerification.OTPVerificationActivity
import com.genesiswtech.lkwb.ui.referCode.ReferCodeActivity
import com.genesiswtech.lkwb.ui.register.model.RegisterResponse
import com.genesiswtech.lkwb.ui.register.presenter.RegisterPresenter
import com.genesiswtech.lkwb.ui.register.view.IRegisterView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(), IRegisterView {

    private lateinit var registerBinding: ActivityRegisterBinding
    private var registerPresenter: RegisterPresenter? = null
    private val REQUEST_ID_MULTIPLE_PERMISSIONS = 101
    private var photoFile: File? = null
    private var mCurrentPhotoPath: String? = null

    private val EMAIL = "email"
    lateinit var callbackManager: CallbackManager

    private val REFER_CODE: Int = 2
    private val RC_SIGN_IN = 3
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private var id: String? = null
    private var email: String? = null
    private var displayName: String? = null
    private var photoUrl: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)
        registerBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        registerBinding.registerhandler = this
        initDependencies()

        //facebook
        callbackManager = CallbackManager.Factory.create()
//gmail
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    override fun initDependencies() {
        registerPresenter = RegisterPresenter(this, application)
    }

    private fun registerApiCall() {
        registerPresenter!!.postRegister(
            this,
            registerBinding.NameEdt.text.toString(),
            registerBinding.emailEdt.text.toString(),
            registerBinding.passwordEdt.text.toString(),
            registerBinding.confirmPasswordEdt.text.toString(),
            mCurrentPhotoPath.toString(), registerBinding.referCodeEdt.text.toString()
        )
    }

    override fun onRegisterButtonClick(v: View?) {
        registerApiCall()
    }

    override fun onFBLoginButtonClick(v: View?) {
        facebookLogin()
    }

    override fun onGoogleLoginButtonClick(v: View?) {
        googleSignIn()
    }

    private fun facebookLogin() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val accessToken = AccessToken.getCurrentAccessToken()
                    val request = GraphRequest.newMeRequest(
                        loginResult!!.accessToken
                    ) { `object`, response ->
                        Log.v("LoginActivity", response.toString())
                        id = `object`!!.getString("id")
                        displayName = `object`.getString("name")
                        email = `object`.getString("email")
                        val photo = `object`.getString("picture")
                        val fbtype: Type = object : TypeToken<FacebookPictureResponse?>() {}.type
                        val fbPic: FacebookPictureResponse = Gson().fromJson(photo, fbtype)
                        photoUrl = fbPic.data!!.url.toString()
                        val pic = "https://graph.facebook.com/" + id + "/picture?type=large"
                        Log.d("TAG", "Picture Url: " + fbPic.data!!.url)
                        Log.d(
                            "TAG", "Picture Url pic: " + photoUrl
                        )

                        type = LKWBConstants.FACEBOOK
                        referCodeIntent()
                    }
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,picture")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    showSnackBar(getString(R.string.login_failed))
                }

                override fun onError(exception: FacebookException) {
                    Log.d("TAG", "Facebook Error: " + exception.message)
                    showSnackBar(getString(R.string.login_failed))
                }

                var accessToken = AccessToken.getCurrentAccessToken()
                var isLoggedIn = accessToken != null && !accessToken!!.isExpired

            })
        LoginManager.getInstance().logInWithReadPermissions(this, listOf(EMAIL, "public_profile"));
    }


    private fun googleSignIn() {
        val intent = mGoogleSignInClient!!.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    private fun referCodeIntent() {
        val intent = Intent(this, ReferCodeActivity::class.java)
        startActivityForResult(intent, REFER_CODE);
    }

    private fun socialLogin(
        type: String, id: String, email: String, name: String, pic: String, referCode: String
    ) {
        registerPresenter!!.postSocialLogin(
            this, type, id, email, name, pic, referCode
        )
    }

    private fun socialLoginApi(
        type: String,
        id: String,
        email: String,
        displayName: String,
        photoUrl: String,
        referCode: String
    ) {
        socialLogin(
            type, id, email, displayName, photoUrl, referCode
        )
    }

    override fun onLoginClick(v: View?) {
        finish()
    }

    override fun onRegisterCLClick(v: View?) {
        if (checkAndRequestPermissions(this)) {
            chooseImage(this)
        }
    }

    override fun onNameError() {
        showSnackBar(getString(R.string.enter_full_name))
    }

    override fun onEmailError() {
        showSnackBar(getString(R.string.enter_email_address))
    }

    override fun onInvalidEmailError() {
        showSnackBar(getString(R.string.enter_valid_email_address))
    }

    override fun onConfirmPasswordError() {
        showSnackBar(getString(R.string.enter_password))
    }

    override fun onPasswordMisMatchError() {
        showSnackBar(getString(R.string.password_mismatch))
    }

    override fun onPasswordError() {
        showSnackBar(getString(R.string.enter_password))
    }

    override fun onImageError() {

    }

    override fun hideKeyboard() {
        AppUtils.hideKeyboard(this)
    }

    override fun onPasswordToggleClick(v: View?) {
        if (registerBinding.passwordEdt.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            registerBinding.passwordEdt.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            registerBinding.passwordEdt.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        registerBinding.passwordEdt.setSelection(registerBinding.passwordEdt.length())
    }

    override fun onConfirmPasswordToggleClick(v: View?) {
        if (registerBinding.confirmPasswordEdt.transformationMethod.equals(
                PasswordTransformationMethod.getInstance()
            )
        ) {
            registerBinding.confirmPasswordEdt.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            registerBinding.confirmPasswordEdt.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        registerBinding.confirmPasswordEdt.setSelection(registerBinding.confirmPasswordEdt.length())
    }

    override fun onReferCodeToggleClick(v: View?) {
        referCodeDialog()
    }

    override fun onSuccess(registerResponse: RegisterResponse) {
        LKWBPreferencesManager.put(registerResponse.data, LKWBConstants.KEY_USER)
        LKWBPreferencesManager.putString(
            LKWBConstants.TOKEN, registerResponse.data!!.token.toString()
        )
        AppUtils.saveProfileData(registerResponse.data!!)

        val intent = Intent(this, OTPVerificationActivity::class.java)
        intent.putExtra(LKWBConstants.EMAIL, registerResponse.data!!.email)
        intent.putExtra(LKWBConstants.REGISTER, true)
        startActivity(intent)
        finish()
    }

    override fun onLoginSuccess(loginResponse: LoginResponse) {
        LKWBPreferencesManager.put(loginResponse.data, LKWBConstants.KEY_USER)
        LKWBPreferencesManager.putString(
            LKWBConstants.TOKEN, loginResponse.data!!.token.toString()
        )
        AppUtils.saveProfileData(loginResponse.data!!)
        if (LKWBPreferencesManager.getString(LKWBConstants.VERIFIED) == getString(R.string.verified)) {
            showSnackBar(getString(R.string.login_success))
//            lifecycleScope.launch(Dispatchers.IO) {
//                println("running task on ${Thread.currentThread().name}")
//                withContext(Dispatchers.Main) {
//                    println("Accessing UI on ${Thread.currentThread().name}")
//                }
//            }
            Executors.newSingleThreadScheduledExecutor().schedule({
                if (loginResponse.data!!.isNewUser == true) {
                    LKWBPreferencesManager.putString(
                        LKWBConstants.IS_NEW_USER, "false"
                    )
                    val intent = Intent(this, LoginRewardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }, 1, TimeUnit.SECONDS)
        } else {
            val intent = Intent(this, OTPVerificationActivity::class.java)
            intent.putExtra(LKWBConstants.VERIFIED, true)
            intent.putExtra(LKWBConstants.EMAIL, loginResponse.data!!.email)
            startActivity(intent)
            finish()
        }

    }

    override fun onLoginFailure(message: String?) {
        showSnackBar(message)
    }

    override fun onLoginTimeOut() {
        showSnackBar(getString(R.string.time_out))
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
        registerBinding.llProgressBar.linear.visibility =
            if (status) View.VISIBLE else View.GONE
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
                    mCurrentPhotoPath = photoFile!!.absolutePath
                    Log.d("TAG", "Camera created Image: " + mCurrentPhotoPath)
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        val photoURI = FileProvider.getUriForFile(
                            this,
                            "com.genesiswtech.lkwb.fileprovider",
                            photoFile!!
                        )
                        takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
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

    private fun checkAndRequestPermissions(context: Activity?): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(
            context!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val cameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                .add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                context, listPermissionsNeeded
                    .toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
                Log.d(
                    "TAG",
                    "Google Login: " + account!!.id + " " + account.email + account.displayName + account.photoUrl
                )
                type = LKWBConstants.GMAIL
                id = account.id.toString()
                email = account.email.toString()
                displayName = account.displayName.toString()
                photoUrl = account.photoUrl.toString()
                referCodeIntent()
            } catch (e: ApiException) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Log.e("TAG", "signInResult:failed code=" + e.statusCode)
                showSnackBar(getString(R.string.login_failed))
            }
        }

        if (requestCode === REFER_CODE) {
            val referCode: String = data!!.getStringExtra(LKWBConstants.REF_CODE).toString()
            socialLoginApi(type!!, id!!, email!!, displayName!!, photoUrl!!, referCode)
        }
        if (resultCode != RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == RESULT_OK && photoFile != null) {
//                0 -> if (resultCode == RESULT_OK && data != null) {
                    val myBitmap = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                    registerBinding.userIV.setImageBitmap(myBitmap)
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
                            registerBinding.userIV.setImageBitmap(
                                BitmapFactory.decodeFile(
                                    picturePath
                                )
                            )
                            cursor.close()
                            mCurrentPhotoPath = picturePath
                        }
                    }
                }
            }
        }
    }

    private fun referCodeDialog() {
        AppUtils.showPurchaseDialog(this,
            getString(R.string.refer_code),
            getString(R.string.refer_code_popup),
            titleOfPositiveButton = getString(R.string.ok),
            positiveButtonFunction = {})
    }

    override val binding: ActivityRegisterBinding
        get() = ActivityRegisterBinding.inflate(layoutInflater)
}