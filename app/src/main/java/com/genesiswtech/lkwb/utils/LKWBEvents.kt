package com.genesiswtech.lkwb.utils

import com.genesiswtech.lkwb.ui.blog.model.BlogDataResponse
import com.genesiswtech.lkwb.ui.dictionarySearch.model.DictionarySearchDataResponse
import com.genesiswtech.lkwb.ui.grammar.model.Grammar

sealed class LKWBEvents {

    data class RemoveFavourites(val id: Int) : LKWBEvents()
    data class AddBlogFavourites(val blogDataResponse: BlogDataResponse) : LKWBEvents()
    data class AddGrammarFavourites(val grammar: Grammar) : LKWBEvents()
    data class AddDictionaryFavourites(val dictionarySearchDataResponse: DictionarySearchDataResponse) :
        LKWBEvents()

    object UpdateImageFromSetting : LKWBEvents()

    object UpdateDiscussionFromDetail : LKWBEvents()

    object UpdateUBTListFromInvoice : LKWBEvents()

    object UpdateUBTListFromResult : LKWBEvents()

    object NavigateToPurchaseTab : LKWBEvents()

    object UpdateBlogFromComment : LKWBEvents()

    object UpdateReplyCount : LKWBEvents()

    object ChangeLanguage : LKWBEvents()

    data class MyEventWithData(val message: String) : LKWBEvents()
    object MyEventWithoutData : LKWBEvents()
}
