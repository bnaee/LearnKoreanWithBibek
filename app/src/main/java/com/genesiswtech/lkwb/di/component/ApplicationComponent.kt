package com.genesiswtech.lkwb


import com.genesiswtech.lkwb.di.module.NetModule
import com.genesiswtech.lkwb.ui.beginTest.BeginTestActivity
import com.genesiswtech.lkwb.ui.beginTest.presenter.BeginTestPresenter
import com.genesiswtech.lkwb.ui.blog.BlogActivity
import com.genesiswtech.lkwb.ui.blog.BlogFragment
import com.genesiswtech.lkwb.ui.blog.presenter.BlogPresenter
import com.genesiswtech.lkwb.ui.blogDetail.BlogDetailActivity
import com.genesiswtech.lkwb.ui.blogDetail.presenter.BlogDetailPresenter
import com.genesiswtech.lkwb.ui.comment.CommentActivity
import com.genesiswtech.lkwb.ui.comment.presenter.CommentPresenter
import com.genesiswtech.lkwb.ui.commentReply.CommentReplyActivity
import com.genesiswtech.lkwb.ui.commentReply.presenter.CommentReplyPresenter
import com.genesiswtech.lkwb.ui.dictionary.DictionaryFragment
import com.genesiswtech.lkwb.ui.dictionary.presenter.DictionaryPresenter
import com.genesiswtech.lkwb.ui.dictionarySearch.DictionarySearchActivity
import com.genesiswtech.lkwb.ui.dictionarySearch.presenter.DictionarySearchPresenter
import com.genesiswtech.lkwb.ui.dictionaryWord.DictionaryWordActivity
import com.genesiswtech.lkwb.ui.dictionaryWord.presenter.DictionaryWordPresenter
import com.genesiswtech.lkwb.ui.discussion.DiscussionFragment
import com.genesiswtech.lkwb.ui.discussion.presenter.DiscussionPresenter
import com.genesiswtech.lkwb.ui.discussionDetail.DiscussionDetailActivity
import com.genesiswtech.lkwb.ui.discussionDetail.presenter.DiscussionDetailsPresenter
import com.genesiswtech.lkwb.ui.discussionDetail.presenter.IDiscussionDetailPresenter
import com.genesiswtech.lkwb.ui.favouriteDetail.FavouriteDetailActivity
import com.genesiswtech.lkwb.ui.favouriteDetail.presenter.FavouriteDetailPresenter
import com.genesiswtech.lkwb.ui.forgotPassword.ForgotPasswordActivity
import com.genesiswtech.lkwb.ui.forgotPassword.presenter.ForgotPasswordPresenter
import com.genesiswtech.lkwb.ui.grammar.GrammarFragment
import com.genesiswtech.lkwb.ui.grammar.presenter.GrammarPresenter
import com.genesiswtech.lkwb.ui.search.SearchActivity
import com.genesiswtech.lkwb.ui.search.presenter.SearchPresenter
import com.genesiswtech.lkwb.ui.grammarWord.GrammarWordActivity
import com.genesiswtech.lkwb.ui.grammarWord.presenter.GrammarWordPresenter
import com.genesiswtech.lkwb.ui.graph.GraphActivity
import com.genesiswtech.lkwb.ui.graph.presenter.GraphPresenter
import com.genesiswtech.lkwb.ui.home.HomeFragment
import com.genesiswtech.lkwb.ui.home.presenter.HomePresenter
import com.genesiswtech.lkwb.ui.login.LoginActivity
import com.genesiswtech.lkwb.ui.login.presenter.LoginPresenter
import com.genesiswtech.lkwb.ui.menu.MenuActivity
import com.genesiswtech.lkwb.ui.menu.presenter.MenuPresenter
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseActivity
import com.genesiswtech.lkwb.ui.mostPurchase.MostPurchaseFragment
import com.genesiswtech.lkwb.ui.mostPurchase.presenter.MostPurchasePresenter
import com.genesiswtech.lkwb.ui.newPassword.NewPasswordActivity
import com.genesiswtech.lkwb.ui.newPassword.presenter.NewPasswordPresenter
import com.genesiswtech.lkwb.ui.notification.NotificationActivity
import com.genesiswtech.lkwb.ui.notification.presenter.NotificationPresenter
import com.genesiswtech.lkwb.ui.otpVerification.OTPVerificationActivity
import com.genesiswtech.lkwb.ui.otpVerification.presenter.OTPVerificationPresenter
import com.genesiswtech.lkwb.ui.register.RegisterActivity
import com.genesiswtech.lkwb.ui.register.presenter.RegisterPresenter
import com.genesiswtech.lkwb.ui.profile.ProfileActivity
import com.genesiswtech.lkwb.ui.profile.presenter.ProfilePresenter
import com.genesiswtech.lkwb.ui.setting.SettingActivity
import com.genesiswtech.lkwb.ui.setting.presenter.SettingPresenter
import com.genesiswtech.lkwb.ui.ubt.UBTTestFragment
import com.genesiswtech.lkwb.ui.ubt.presenter.UBTTestPresenter
import com.genesiswtech.lkwb.ui.ubtBuy.UBTBuyActivity
import com.genesiswtech.lkwb.ui.ubtBuy.presenter.UBTBuyPresenter
import com.genesiswtech.lkwb.ui.ubtQuestion.UBTQuestionActivity
import com.genesiswtech.lkwb.ui.ubtQuestion.presenter.UBTQuestionPresenter
import dagger.Component

@Component(modules = [AppModule::class, NetModule::class])
interface ApplicationComponent {
    fun inject(mLKWBApplication: LKWBApplication)

    fun inject(mLoginPresenter: LoginPresenter)
    fun inject(mLoginActivity: LoginActivity)

    fun inject(mRegisterActivity: RegisterActivity)
    fun inject(mRegisterPresenter: RegisterPresenter)

    fun inject(mForgotPasswordActivity: ForgotPasswordActivity)
    fun inject(mForgotPasswordPresenter: ForgotPasswordPresenter)

    fun inject(mOTPVerificationPresenter: OTPVerificationPresenter)
    fun inject(mOTPVerificationActivity: OTPVerificationActivity)

    fun inject(mNewPasswordActivity: NewPasswordActivity)
    fun inject(mNewPasswordPresenter: NewPasswordPresenter)

    fun inject(mHomePresenter: HomePresenter)
    fun inject(mHomeFragment: HomeFragment)

    fun inject(mDictionaryPresenter: DictionaryPresenter)
    fun inject(mDictionaryFragment: DictionaryFragment)

    fun inject(mGrammarFragment: GrammarFragment)
    fun inject(mGrammarPresenter: GrammarPresenter)

    fun inject(mGrammarWordActivity: GrammarWordActivity)
    fun inject(mGrammarWordPresenter: GrammarWordPresenter)

    fun inject(mDictionarySearchActivity: DictionarySearchActivity)
    fun inject(mDictionarySearchPresenter: DictionarySearchPresenter)

    fun inject(mDictionaryWordActivity: DictionaryWordActivity)
    fun inject(mDictionaryWordPresenter: DictionaryWordPresenter)

    fun inject(mBlogPresenter: BlogPresenter)
    fun inject(mBlogFragment: BlogFragment)
    fun inject(mBlogActivity: BlogActivity)

    fun inject(mMenuActivity: MenuActivity)
    fun inject(mMenuPresenter: MenuPresenter)

    fun inject(mProfilePresenter: ProfilePresenter)
    fun inject(mProfileActivity: ProfileActivity)

    fun inject(mBlockDetailActivity: BlogDetailActivity)
    fun inject(mBlockDetailPresenter: BlogDetailPresenter)

    fun inject(mUBTTestFragment: UBTTestFragment)
    fun inject(mUBTTestPresenter: UBTTestPresenter)

    fun inject(mBeginTestActivity: BeginTestActivity)
    fun inject(mBeginTestPresenter: BeginTestPresenter)

    fun inject(mUBTQuestionActivity: UBTQuestionActivity)
    fun inject(mUBTQuestionPresenter: UBTQuestionPresenter)

    fun inject(mUBTBuyPresenter: UBTBuyPresenter)
    fun inject(mUBTBuyActivity: UBTBuyActivity)

    fun inject(mFavouriteDetailPresenter: FavouriteDetailPresenter)
    fun inject(mFavouriteDetailActivity: FavouriteDetailActivity)

    fun inject(mGrammarSearchPresenter: SearchPresenter)
    fun inject(mGrammarSearchActivity: SearchActivity)


    fun inject(mMostPurchaseFragment: MostPurchaseFragment)
    fun inject(mMostPurchasePresenter: MostPurchasePresenter)
    fun inject(mMostPurchaseActivity: MostPurchaseActivity)

    fun inject(mSettingPresenter: SettingPresenter)
    fun inject(mSettingActivity: SettingActivity)

    fun inject(mDiscussionPresenter: DiscussionPresenter)
    fun inject(mDiscussionFragment: DiscussionFragment)

    fun inject(mDiscussionDetailPresenter: IDiscussionDetailPresenter)
    fun inject(mDiscussionDetailsPresenter: DiscussionDetailsPresenter)
    fun inject(mDiscussionDetailActivity: DiscussionDetailActivity)

    fun inject(mCommentReplyPresenter: CommentReplyPresenter)
    fun inject(mCommentReplyActivity: CommentReplyActivity)

    fun inject(mNotificationPresenter: NotificationPresenter)
    fun inject(mNotificationActivity: NotificationActivity)

    fun inject(mCommentPresenter: CommentPresenter)
    fun inject(mCommentActivity: CommentActivity)

    fun inject(mGraphPresenter: GraphPresenter)
    fun inject(mGraphActivity: GraphActivity)

}