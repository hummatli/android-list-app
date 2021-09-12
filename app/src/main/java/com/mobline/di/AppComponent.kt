package com.mobline.di

import com.mobline.appinitializers.di.initModule
import com.mobline.data.di.dataModule
import com.mobline.domain.di.domainModule
import com.mobline.presentation.di.presentationModule

val appComponent = listOf(
    initModule,
    dataModule,
    domainModule,
    presentationModule,
)