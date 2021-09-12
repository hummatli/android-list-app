package com.mobline.appinitializers.di

import com.mobline.appinitializers.AppInitializer
import com.mobline.appinitializers.TimberInitializer
import org.koin.dsl.module

val initModule = module {
    single<AppInitializer> { TimberInitializer() }
}