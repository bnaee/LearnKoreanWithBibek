package com.genesiswtech.lkwb.utils

import org.koin.dsl.module

class LKWBEventModule {
}

val appModule = module {
    single { LKWBEventBus() }
}