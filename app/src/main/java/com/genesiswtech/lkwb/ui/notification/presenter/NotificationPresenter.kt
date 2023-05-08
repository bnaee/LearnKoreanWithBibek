package com.genesiswtech.lkwb.ui.notification.presenter

import android.app.Application
import android.content.Context
import com.genesiswtech.lkwb.LKWBApplication
import com.genesiswtech.lkwb.R
import com.genesiswtech.lkwb.base.BasePresenter
import com.genesiswtech.lkwb.network.INetworkApi
import com.genesiswtech.lkwb.ui.notification.model.NotificationResponse
import com.genesiswtech.lkwb.ui.notification.model.ReadNotificationResponse
import com.genesiswtech.lkwb.ui.notification.view.INotificationView
import com.genesiswtech.lkwb.utils.AppUtils
import com.genesiswtech.lkwb.utils.InternetConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class NotificationPresenter(
    private var notificationView: INotificationView,
    var applicationComponent: Application
) :
    INotificationPresenter, BasePresenter<INotificationView>(notificationView) {

    @Inject
    lateinit var mNetworkApi: INetworkApi

    init {
        (applicationComponent as LKWBApplication).applicationComponent.inject(this)
    }

    override fun getNotifications(context: Context, page: Int, limit: Int) {
        if (InternetConnection.checkForInternet(context)) {
            notificationView.onChooseProgressBar(page, true)
            mNetworkApi.getNotifications(page, limit).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<NotificationResponse>? ->
                    notificationView.onChooseProgressBar(page, false)
                    if (response!!.isSuccessful) {
                        if (response.body()!!.data != null) {
                            notificationView.onSuccess(response.body()!!.data)
                            notificationView.onNoData(false)
                        } else {
                            if (page == 1)
                                notificationView.onNoData(true)
                        }
                    } else {
                        notificationView.onChooseProgressBar(page, false)
                        notificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    notificationView.onChooseProgressBar(page, false)
                    notificationView.onFailure(error.localizedMessage)
                })

        } else {
            notificationView.onChooseProgressBar(page, false)
            notificationView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

    override fun readNotification(context: Context, id: Int) {
        if (InternetConnection.checkForInternet(context)) {
            notificationView.onShowProgressBar(false)
            mNetworkApi.postReadNotification(id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response: Response<ReadNotificationResponse>? ->
                    notificationView.onShowProgressBar(false)
                    if (response!!.isSuccessful) {
                        notificationView.onReadSuccess(response.body()!!)
                    } else {
                        notificationView.onShowProgressBar(false)
                        notificationView.onFailure(AppUtils.getErrorMessage(response.errorBody()))
                    }
                }, { error ->
                    notificationView.onShowProgressBar(false)
                    notificationView.onFailure(error.localizedMessage)
                })
        } else {
            notificationView.onShowProgressBar(false)
            notificationView.showSnackBar(context.getString(R.string.no_internet))
        }
    }

}