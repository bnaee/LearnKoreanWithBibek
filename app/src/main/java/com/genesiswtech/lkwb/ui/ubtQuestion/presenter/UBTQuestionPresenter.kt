package com.genesiswtech.lkwb.ui.ubtQuestion.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.ui.ubtQuestion.model.StoreResultResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.view.IUBTQuestionView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class UBTQuestionPresenter(
    var ubtQuestionView: IUBTQuestionView,
    var applicationComponent: Application
) :
    IUBTQuestionPresenter, BasePresenter<IUBTQuestionView>(ubtQuestionView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getUBTQuestion(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            ubtQuestionView.onShowProgressBar(true)
            mNetworkApi.getUBTQuestions(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<UBTQuestionResponse>? ->
                    ubtQuestionView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        response.body()?.let { ubtQuestionView.onSuccess(it) }
                    } else {
                        ubtQuestionView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtQuestionView.onShowProgressBar(false)
                    ubtQuestionView.onFailure(error.localizedMessage)
                })

        } else {
            ubtQuestionView.onShowProgressBar(false)
            ubtQuestionView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun postStoreResult(
        context: Context,
        attempted_questions: String,
        unsolved_questions: String,
        time_spent: String,
        correct_answers: String,
        score: String,
        set_id: String, package_id: String
    ) {
        if (InternetConnection.checkForInternet(context)) {
            ubtQuestionView.onShowProgressBar(true)
            mNetworkApi.postStoreResult(
                attempted_questions,
                unsolved_questions,
                time_spent,
                correct_answers,
                score,
                set_id, package_id
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<StoreResultResponse>? ->
                    ubtQuestionView.onShowProgressBar(true)
                    if (response!!.isSuccessful) {
                        response.body()!!.data?.let { ubtQuestionView.onStoreResultSuccess(it) }
                    } else {
                        ubtQuestionView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    ubtQuestionView.onShowProgressBar(false)
                    ubtQuestionView.onFailure(error.localizedMessage)
                })

        } else {
            ubtQuestionView.onShowProgressBar(false)
            ubtQuestionView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}