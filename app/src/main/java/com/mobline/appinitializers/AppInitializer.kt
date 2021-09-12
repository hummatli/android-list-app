package com.mobline.appinitializers

import android.app.Application

interface AppInitializer {
    fun init(application: Application)
}
