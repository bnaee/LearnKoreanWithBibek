package com.genesiswtech.lkwb.ui.grammar.model

import com.genesiswtech.lkwb.ui.dictionary.model.Dictionary
import com.genesiswtech.lkwb.ui.dictionary.model.DictionaryDayResponse
import com.genesiswtech.lkwb.ui.dictionaryWord.model.DictionaryWordDataResponse
import com.google.gson.annotations.SerializedName

data class GrammarDayDataResponse(
    @SerializedName("grammar") var grammar: Grammar? = Grammar(),
    @SerializedName("dictionary") var dictionary: Dictionary? = Dictionary()
)