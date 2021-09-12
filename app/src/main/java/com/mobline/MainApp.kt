package com.mobline

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.Settings
import com.mobline.appinitializers.AppInitializer
import com.mobline.marleyspoon.BuildConfig
import com.mobline.di.appComponent
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MainApp : Application() {

    private val initializers by inject<AppInitializer>()

    @SuppressLint("HardwareIds")
    override fun onCreate() {
        super.onCreate()

        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val deviceModel = if (model.startsWith(manufacturer)) {
            model
        } else "$manufacturer $model"

        val androidID = Settings.Secure.getString(
            this.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        startKoin {
            androidContext(androidContext = this@MainApp)
            properties(
                mapOf(
                    "host" to BuildConfig.API_URL,
                    "spaceId" to BuildConfig.API_SPACE_ID,
                    "environmentId" to BuildConfig.API_ENVIRONMENT_ID,
                    "accessToken" to BuildConfig.API_ACCESS_TOKEN,
                    "version" to "${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})",
                    "versionCode" to "${BuildConfig.VERSION_CODE}",
                    "osVersion" to Build.VERSION.RELEASE,
                    "deviceModel" to deviceModel,
                    "deviceID" to androidID,
                    "isDebug" to (BuildConfig.DEBUG || BuildConfig.VERSION_NAME.contains("-dev")).toString(),
                    "defaultClipboardLabel" to "Label",
                    "fileHost" to BuildConfig.FILE_STORAGE_URL
                )
            ) //API-HOST
            modules(appComponent)
        }

        initializers.init(this)
    }

}
