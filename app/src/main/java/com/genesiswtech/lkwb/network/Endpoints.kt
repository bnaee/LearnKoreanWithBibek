package com.genesiswtech.lkwb.network

import com.genesiswtech.lkwb.BuildConfig

object Endpoints {

    val BASE_URL: String = getBaseUrl()

    const val login = "login"
    const val login_social = "login/{type}}/callback"
    const val logout = "logout"

    const val register = "register"
    const val forgot_password = "forgot-password"
    const val verify_otp = "verify-otp/{email}"
    const val create_new_password = "create-new-password"
    const val resend_otp = "resend-otp"
    const val new_user_verify_otp = "verify-otp"

    const val home_services = "home/services"
    const val home_banner = "home/banner"
    const val home_most_bought = "home/most-bought-sets"
    const val new_test = "home/new-sets"
    const val last_test_score = "last-test-score"
    const val result_history_app = "result-history-app"

    const val grammar_day = "words-of-today"
    const val grammar_all = "home/all-grammar"
    const val grammar_category = "home/grammar-category"
    const val grammar_single = "home/grammar-word/{id}"

    const val dictionaries = "home/dictionaries"
    const val single_dictionary = "home/dictionaries/{id}"

    const val single_blogs = "home/blogs/{id}"
    const val blogs = "home/blogs"

    const val all_packages = "home/all-packages"
    const val ubt_test = "home/all-sets"
    const val ubt_set = "sets/{id}"
    const val ubt_questions = "sets/{id}/questions"
    const val ubt_buy = "{sets}/{id}/buy/{type}"
    const val all_packages_list = "all-packages/{id}"

    const val profile = "profile"
    const val update_profile = "update-profile"

    const val add_to_favorites = "add-to-favorites"
    const val remove_from_favorites = "remove-from-favorites"

    const val favourite_blog = "favorites/blog"
    const val favourite_grammar = "favorites/grammar"
    const val favourite_dictionary = "favorites/dictionary"

    const val store_result = "sets/store-result"

    const val discussions = "home/discussions"
    const val single_discussions = "home/discussions/{id}"
    const val create_discussions = "discussions/create"
    const val delete_discussions = "discussions/{id}/delete"
    const val edit_discussions = "discussions/{id}/update"

    const val comments = "discussions/{id}/comments"
    const val delete_comment = "comments/{id}/delete"
    const val edit_comment = "comment/{id}/update"
    const val post_comment = "discussions/{id}/comment"

    const val comment_replies = "comments/{id}/replies"
    const val reply_update = "reply/{id}/update"
    const val reply_delete = "reply/{id}/delete"
    const val post_reply = "comment/{id}/reply"

    const val blog_comments = "home/blogs/{id}/comments"
    const val blog_post_comments = "blog/{id}/comment"

    const val search = "search"

    const val notifications = "notifications"
    const val notifications_read = "notifications/{id}/update"

    const val delete_account = "delete-account"

    const val store_device_token = "store-device-token/web"

    const val reclaim = "store/transaction-claims"

    private fun getBaseUrl(): String {
        return if (BuildConfig.DEBUG) {
            BuildConfig.PROTOCOL_DEBUG + "://" + BuildConfig.BACKEND_DEBUG + "/api/"
        } else {
            BuildConfig.PROTOCOL_RELEASE + "://" + BuildConfig.BACKEND_RELEASE + "/api/"
        }
    }
}