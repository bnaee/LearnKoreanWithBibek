package com.genesiswtech.lkwb.network


import com.genesiswtech.lkwb.data.FavouriteResponse
import com.genesiswtech.lkwb.firebase.data.StorePushTokenResponse
import com.genesiswtech.lkwb.ui.beginTest.model.BeginTestResponse
import com.genesiswtech.lkwb.ui.blog.model.BlogResponse
import com.genesiswtech.lkwb.ui.blogDetail.model.BlogDetailResponse
import com.genesiswtech.lkwb.ui.commentReply.model.CommentReplyResponse
import com.genesiswtech.lkwb.ui.commentReply.model.ReplyEditResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionPostResponse
import com.genesiswtech.lkwb.ui.discussion.model.DiscussionResponse
import com.genesiswtech.lkwb.ui.discussionDetail.model.*
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailBlogResponse
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailDictionaryResponse
import com.genesiswtech.lkwb.ui.favouriteDetail.model.FavouriteDetailGrammarResponse
import com.genesiswtech.lkwb.ui.forgotPassword.model.ForgotPasswordResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarCategoryResponse
import com.genesiswtech.lkwb.ui.grammar.model.GrammarDayResponse
import com.genesiswtech.lkwb.ui.grammarWord.model.GrammarWordResponse
import com.genesiswtech.lkwb.ui.home.model.*
import com.genesiswtech.lkwb.ui.login.model.LoginResponse
import com.genesiswtech.lkwb.ui.menu.model.LogOutResponse
import com.genesiswtech.lkwb.ui.menu.model.ReclaimResponse
import com.genesiswtech.lkwb.ui.mostPurchase.model.PackageListResponse
import com.genesiswtech.lkwb.ui.newPassword.model.NewPasswordResponse
import com.genesiswtech.lkwb.ui.notification.model.NotificationResponse
import com.genesiswtech.lkwb.ui.notification.model.ReadNotificationResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.AccountDeleteResponse
import com.genesiswtech.lkwb.ui.otpVerification.model.OTPVerificationResponse
import com.genesiswtech.lkwb.ui.profile.model.ProfileResponse
import com.genesiswtech.lkwb.ui.profile.model.UpdateProfileResponse
import com.genesiswtech.lkwb.ui.register.model.RegisterResponse
import com.genesiswtech.lkwb.ui.search.model.GrammarSearchResponse
import com.genesiswtech.lkwb.ui.ubt.model.PackageResponse
import com.genesiswtech.lkwb.ui.ubt.model.UBTTestResponse
import com.genesiswtech.lkwb.ui.ubtBuy.model.UBTBuyResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.StoreResultResponse
import com.genesiswtech.lkwb.ui.ubtQuestion.model.UBTQuestionResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*


interface INetworkApi {

    @FormUrlEncoded
    @POST(Endpoints.login)
    @Headers("No-Authentication: true")
    fun postLogin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Observable<Response<LoginResponse>>

    @FormUrlEncoded
    @POST(Endpoints.login_social)
    @Headers("No-Authentication: true")
    fun postLoginSocial(
        @Path("type") type: String?,
        @Field("provider_id") oid: String?,
        @Field("email") email: String?,
        @Field("name") name: String?,
        @Field("photo") photo: String?,
        @Field("referCode") referCode: String?
    ): Observable<Response<LoginResponse>>

    @Multipart
    @POST(Endpoints.register)
    @Headers("No-Authentication: true")
    fun postRegister(
        @Part("name") name: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part("password") password: RequestBody?,
        @Part("password_confirmation") password_confirmation: RequestBody?,
        @Part file: MultipartBody.Part?,
        @Part("referCode") referCode: RequestBody?,
    ): Observable<Response<RegisterResponse>>

    @FormUrlEncoded
    @POST(Endpoints.forgot_password)
    @Headers("No-Authentication: true")
    fun postForgotPassword(
        @Field("email") email: String?
    ): Observable<Response<ForgotPasswordResponse>>

    @FormUrlEncoded
    @POST(Endpoints.verify_otp)
    @Headers("No-Authentication: true")
    fun postVerifyOTP(
        @Path("email") email: String?,
        @Field("otp") otp: String?
    ): Observable<Response<OTPVerificationResponse>>

    @FormUrlEncoded
    @POST(Endpoints.new_user_verify_otp)
    fun postNewUserVerifyOTP(
        @Field("otp") otp: String?
    ): Observable<Response<OTPVerificationResponse>>

    @FormUrlEncoded
    @POST(Endpoints.create_new_password)
    fun postNewPassword(
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?
    ): Observable<Response<NewPasswordResponse>>

    @POST(Endpoints.logout)
    fun postLogOut(): Observable<Response<LogOutResponse>>

    @GET(Endpoints.home_services)
    fun getServices(): Observable<Response<ServiceResponse>>

    @GET(Endpoints.home_most_bought)
    fun getMostBought(
        @Query("page") page: String?,
        @Query("limit") limit: String?
    ): Observable<Response<MostBoughtResponse>>

    @GET(Endpoints.new_test)
    fun getNewTest(
        @Query("page") page: String?,
        @Query("limit") limit: String?
    ): Observable<Response<MostBoughtResponse>>

    @GET(Endpoints.home_banner)
    fun getBanner(): Observable<Response<BannerResponse>>

    @GET(Endpoints.result_history_app)
    fun getResultHistoryApp(): Observable<Response<ResultHistoryResponse>>

    @GET(Endpoints.grammar_day)
    fun getGrammarDay(): Observable<Response<GrammarDayResponse>>

    @GET(Endpoints.grammar_category)
    fun getGrammarCategory(): Observable<Response<GrammarCategoryResponse>>

    @GET(Endpoints.grammar_all)
    fun getGrammarAll(): Observable<Response<GrammarSearchResponse>>

    @GET(Endpoints.grammar_single)
    fun getGrammarSingle(@Path("id") id: Int?): Observable<Response<GrammarWordResponse>>

    @GET(Endpoints.dictionaries)
    fun getDictionaries(
        @Query("page") page: String?,
        @Query("limit") limit: String?
    ): Observable<Response<DictionarySearchResponse>>

    @GET(Endpoints.single_dictionary)
    fun getSingleDictionary(@Path("id") id: Int?): Observable<Response<DictionaryWordResponse>>

    @GET(Endpoints.single_blogs)
    fun getSingleBlog(@Path("id") id: Int?): Observable<Response<BlogDetailResponse>>

    @GET(Endpoints.blogs)
    @Headers("No-Authentication: true")
    fun getBlogs(): Observable<Response<BlogResponse>>

    @GET(Endpoints.all_packages)
    fun getAllPackages(): Observable<Response<PackageResponse>>

    @GET(Endpoints.ubt_test)
    fun getUBTTest(
        @Query("page") page: String?,
        @Query("limit") limit: String?,
        @Query("setType") setType: String?,
        @Query("priceSort") priceSort: String?
    ): Observable<Response<UBTTestResponse>>

    @GET(Endpoints.ubt_set)
    fun getUBTSet(@Path("id") id: Int?): Observable<Response<BeginTestResponse>>

    @GET(Endpoints.all_packages_list)
    fun getPackageList(
        @Path("id") id: Int?, @Query("setType") setType: String?, @Query("page") page: String?,
        @Query("limit") limit: String?
    ): Observable<Response<PackageListResponse>>

    @GET(Endpoints.ubt_questions)
    fun getUBTQuestions(@Path("id") id: Int?): Observable<Response<UBTQuestionResponse>>

    @FormUrlEncoded
    @POST(Endpoints.update_profile)
    fun putUpdateProfile(
        @Field("name") name: String?,
        @Field("_method") method: String?,
        @Field("password") password: String?,
        @Field("password_confirmation") password_confirmation: String?,
    ): Observable<Response<UpdateProfileResponse>>

    @FormUrlEncoded
    @POST(Endpoints.update_profile)
    fun putUpdateFullName(
        @Field("name") name: String?,
        @Field("_method") method: String?
    ): Observable<Response<UpdateProfileResponse>>

    @GET(Endpoints.last_test_score)
    fun getLastTestScore(): Observable<Response<TestScoreResponse>>

    @FormUrlEncoded
    @POST(Endpoints.add_to_favorites)
    fun postAddFavourite(
        @Field("favoritable_id") favoritable_id: Int?,
        @Field("favoritable_type") favoritable_type: String?
    ): Observable<Response<FavouriteResponse>>

    @FormUrlEncoded
    @POST(Endpoints.remove_from_favorites)
    fun postRemoveFavourite(
        @Field("favoritable_id") favoritable_id: Int?,
        @Field("favoritable_type") favoritable_type: String?
    ): Observable<Response<FavouriteResponse>>

    @FormUrlEncoded
    @POST(Endpoints.ubt_buy)
    fun postUBTBuy(
        @Path("sets") sets: String?, @Path("id") id: Int?, @Path("type") type: String?,
        @Field("oid") oid: String?,
        @Field("amt") amt: String?,
        @Field("refId") refId: String?,
        @Field("mobile") mobile: Boolean?,
    ): Observable<Response<UBTBuyResponse>>

    @FormUrlEncoded
    @POST(Endpoints.ubt_buy)
    fun postUBTKhaltiBuy(
        @Path("sets") sets: String?, @Path("id") id: Int?, @Path("type") type: String?,
        @Field("amt") amt: String?,
        @Field("isMobile") isMobile: Boolean?,
        @Field("pidx") pidx: String?,
        @Field("poid") poid: String?,
        @Field("pon") pon: String?,
        @Field("txn_id") txn_id: String?,
        @Field("mobile") mobile: String?
    ): Observable<Response<UBTBuyResponse>>

    @GET(Endpoints.favourite_blog)
    fun getFavouriteBlog(): Observable<Response<FavouriteDetailBlogResponse>>

    @GET(Endpoints.favourite_grammar)
    fun getFavouriteGrammar(): Observable<Response<FavouriteDetailGrammarResponse>>

    @GET(Endpoints.favourite_dictionary)
    fun getFavouriteDictionary(): Observable<Response<FavouriteDetailDictionaryResponse>>

    @FormUrlEncoded
    @POST(Endpoints.store_result)
    fun postStoreResult(
        @Field("attempted_questions") attempted_questions: String?,
        @Field("unsolved_questions") unsolved_questions: String?,
        @Field("time_spent") time_spent: String?,
        @Field("correct_answers") correct_answers: String?,
        @Field("score") score: String?,
        @Field("set_id") set_id: String?,
        @Field("package_id") package_id: String?
    ): Observable<Response<StoreResultResponse>>

    @Multipart
    @POST(Endpoints.update_profile)
    fun putUpdateProfilePicture(
        @Part file: MultipartBody.Part?,
        @Part("_method") items: RequestBody?
    ): Observable<Response<UpdateProfileResponse>>

    @GET(Endpoints.discussions)
    fun getDiscussions(
        @Query("page") page: String?,
        @Query("limit") limit: String?,
        @Query("sort_order") sort_order: String?
    ): Observable<Response<DiscussionResponse>>

    @FormUrlEncoded
    @POST(Endpoints.create_discussions)
    fun postNewDiscussion(
        @Field("title") title: String?,
        @Field("description") description: String?
    ): Observable<Response<DiscussionPostResponse>>

//    @GET(Endpoints.single_discussions)
//    fun getSingleDiscussion(@Path("id") id: Int?): Observable<Response<DiscussionDetailResponse>>

    @GET(Endpoints.single_discussions)
    fun getSingleDiscussion(@Path("id") id: Int?): Observable<Response<DiscussionDetailResponse>>

    @FormUrlEncoded
    @POST(Endpoints.edit_discussions)
    fun postUpdateDiscussion(
        @Path("id") id: Int?,
        @Field("_method") method: String?,
        @Field("title") title: String?,
        @Field("description") description: String?
    ): Observable<Response<DiscussionEditResponse>>

    @FormUrlEncoded
    @POST(Endpoints.delete_discussions)
    fun postDeleteDiscussion(
        @Path("id") id: Int?,
        @Field("_method") method: String?
    ): Observable<Response<DiscussionDetailResponse>>

    @GET(Endpoints.single_discussions)
    fun getDiscussionSingle(@Path("id") id: Int?): Observable<Response<DiscussionDetailResponse>>

    @FormUrlEncoded
    @POST(Endpoints.search)
    fun postGrammarSearch(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Field("model") type: String?,
        @Field("param") text: String?
    ): Observable<Response<GrammarSearchResponse>>

    @FormUrlEncoded
    @POST(Endpoints.search)
    fun postDictionarySearch(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Field("model") type: String?,
        @Field("param") text: String?
    ): Observable<Response<DictionarySearchResponse>>

    @FormUrlEncoded
    @POST(Endpoints.search)
    fun postBlogSearch(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Field("model") type: String?,
        @Field("param") text: String?
    ): Observable<Response<BlogResponse>>

    @FormUrlEncoded
    @POST(Endpoints.search)
    fun postDiscussionSearch(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?,
        @Field("model") type: String?,
        @Field("param") text: String?
    ): Observable<Response<DiscussionResponse>>

    @GET(Endpoints.blog_comments)
    fun getBlogComments(
        @Path("id") id: Int?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Observable<Response<CommentResponse>>

    @FormUrlEncoded
    @POST(Endpoints.blog_post_comments)
    fun postBlogComment(
        @Path("id") id: Int?,
        @Field("body") body: String?
    ): Observable<Response<CommentPostResponse>>

    @GET(Endpoints.comments)
    fun getComments(
        @Path("id") id: Int?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Observable<Response<CommentResponse>>

    @FormUrlEncoded
    @POST(Endpoints.post_comment)
    fun postComment(
        @Path("id") id: Int?,
        @Field("body") body: String?
    ): Observable<Response<CommentPostResponse>>

    @FormUrlEncoded
    @POST(Endpoints.edit_comment)
    fun postUpdateComment(
        @Path("id") id: Int?,
        @Field("_method") method: String?,
        @Field("body") body: String?
    ): Observable<Response<CommentEditResponse>>

    @FormUrlEncoded
    @POST(Endpoints.delete_comment)
    fun postDeleteComment(
        @Path("id") id: Int?,
        @Field("_method") method: String?
    ): Observable<Response<CommentResponse>>

    @GET(Endpoints.comment_replies)
    fun getCommentReplies(
        @Path("id") id: Int?,
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Observable<Response<CommentReplyResponse>>

    @FormUrlEncoded
    @POST(Endpoints.post_reply)
    fun postReply(
        @Path("id") id: Int?,
        @Field("body") body: String?
    ): Observable<Response<ReplyEditResponse>>

    @FormUrlEncoded
    @POST(Endpoints.reply_update)
    fun postUpdateReply(
        @Path("id") id: Int?,
        @Field("_method") method: String?,
        @Field("body") body: String?
    ): Observable<Response<ReplyEditResponse>>

    @FormUrlEncoded
    @POST(Endpoints.reply_delete)
    fun postDeleteReply(
        @Path("id") id: Int?,
        @Field("_method") method: String?
    ): Observable<Response<CommentReplyResponse>>

    @FormUrlEncoded
    @POST(Endpoints.resend_otp)
    @Headers("No-Authentication: true")
    fun postResendOTP(
        @Field("email") email: String?
    ): Observable<Response<ForgotPasswordResponse>>

    @GET(Endpoints.notifications)
    fun getNotifications(
        @Query("page") page: Int?,
        @Query("limit") limit: Int?
    ): Observable<Response<NotificationResponse>>

    @POST(Endpoints.notifications_read)
    fun postReadNotification(
        @Path("id") id: Int?
    ): Observable<Response<ReadNotificationResponse>>

    @FormUrlEncoded
    @POST(Endpoints.delete_account)
    fun postDeleteAccount(
        @Field("_method") method: String?,
        @Field("password") password: String?,
        @Field("otp") otp: String?,
    ): Observable<Response<AccountDeleteResponse>>

    @GET(Endpoints.profile)
    fun getProfile(): Observable<Response<ProfileResponse>>

    @FormUrlEncoded
    @POST(Endpoints.store_device_token)
    fun postDeviceToken(
        @Field("device_id") method: String?,
        @Field("device_type") password: String?,
    ): Observable<Response<StorePushTokenResponse>>

    @FormUrlEncoded
    @POST(Endpoints.reclaim)
    fun postReclaim(
        @Field("txn_id") txn_id: String?,
        @Field("gateway") gateway: String?
    ): Observable<Response<ReclaimResponse>>

}