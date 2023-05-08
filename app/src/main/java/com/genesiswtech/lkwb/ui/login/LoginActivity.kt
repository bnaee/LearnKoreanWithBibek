package com.genesiswtech.lkwb.ui.login


import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BaseActivity
import com.genesiswtech.lkwb.data.FacebookPictureResponse
import com.genesiswtech.lkwb.databinding.ActivityLoginBinding
import com.genesiswtech.lkwb.databinding.ActivityProfileBinding
import com.genesiswtech.lkwb.databinding.ActivitySettingBinding
import com.genesiswtech.lkwb.ui.forgotPassword.ForgotPasswordActivity
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.login.presenter.LoginPresenter
import com.genesiswtech.lkwb.ui.login.view.ILoginView
import com.genesiswtech.lkwb.ui.loginReward.LoginRewardActivity
import com.genesiswtech.lkwb.ui.main.MainActivity
import com.genesiswtech.lkwb.ui.otpVerification.OTPVerificationActivity
import com.genesiswtech.lkwb.ui.referCode.ReferCodeActivity
import com.genesiswtech.lkwb.ui.register.RegisterActivity
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
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class LoginActivity : BaseActivity<ActivityLoginBinding>(), ILoginView {

    private lateinit var loginBinding: ActivityLoginBinding
    private var loginPresenter: LoginPresenter? = null
    private val EMAIL = "email"
    lateinit var callbackManager: CallbackManager

    private val REFER_CODE: Int = 2
    private val RC_SIGN_IN = 1
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private var id: String? = null
    private var email: String? = null
    private var displayName: String? = null
    private var photoUrl: String? = null
    private var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtils.windowBackground(this)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginBinding.loginhandler = this
        initDependencies()
//facebook
        callbackManager = CallbackManager.Factory.create()
//gmail
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    override fun initDependencies() {
        loginPresenter = LoginPresenter(this, application)
    }

    override fun onLoginButtonClick(v: View?) {
        loginPresenter!!.postLogin(
            this,
            loginBinding.emailEdt.text.toString().trim(),
            loginBinding.passwordEdt.text.toString().trim()
        )
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
                        loginResult.accessToken
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

    private fun socialLogin(
        type: String, id: String, email: String, name: String, pic: String, referCode: String
    ) {
        loginPresenter!!.postSocialLogin(
            this, type, id, email, name, pic, referCode
        )
    }

    override fun onForgotPasswordClick(v: View?) {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    override fun onRegisterClick(v: View?) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onPasswordToggleClick(v: View?) {
        if (loginBinding.passwordEdt.transformationMethod.equals(PasswordTransformationMethod.getInstance())) {
            loginBinding.passwordEdt.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
        } else {
            loginBinding.passwordEdt.transformationMethod =
                PasswordTransformationMethod.getInstance()
        }
        loginBinding.passwordEdt.setSelection(loginBinding.passwordEdt.length())
    }

    override fun hideKeyboard() {
        AppUtils.hideKeyboard(this)
    }

    override fun onEmailError() {
        showSnackBar(getString(R.string.enter_email_address))
    }

    override fun onPasswordError() {
        showSnackBar(getString(R.string.enter_password))
    }

    override fun onInvalidEmailError() {
        showSnackBar(getString(R.string.enter_valid_email_address))
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

    override fun showSnackBar(message: String?) {
        AppUtils.showSnackBar(message.toString(), this)
    }

    override fun onShowProgressBar(status: Boolean) {
        loginBinding.llProgressBar.linear.visibility = if (status) View.VISIBLE else View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
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
    }

    private fun referCodeIntent() {
        val intent = Intent(this, ReferCodeActivity::class.java)
        startActivityForResult(intent, REFER_CODE);
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

    override val binding: ActivityLoginBinding
        get() = ActivityLoginBinding.inflate(layoutInflater)

}