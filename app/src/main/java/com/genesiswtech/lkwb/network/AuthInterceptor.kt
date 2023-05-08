package com.genesiswtech.lkwb.network

import com.genesiswtech.lkwb.ui.login.model.LoginDataResponse
import com.genesiswtech.lkwb.utils.LKWBConstants
import com.genesiswtech.lkwb.utils.LKWBPreferencesManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    private val userData = LKWBPreferencesManager.get<LoginDataResponse>(LKWBConstants.KEY_USER)
    private val token = LKWBPreferencesManager.getString(LKWBConstants.TOKEN)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        if (request.header("No-Authentication") == null) {
//            userData!!.token?.let {
//                requestBuilder.addHeader("Authorization", "Bearer $it")
//            }
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())


    }
}