package com.mobline.presentation.flow.splash

import com.mobline.presentation.base.BaseViewModel
import kotlinx.coroutines.delay

class SplashViewModel : BaseViewModel<Nothing, Unit>() {

    init {
        launchAll(loadingHandle = {}) {
            delay(2000)
            navigate(SplashFragmentDirections.actionSplashFragmentToMainPageFragment())
        }
    }
}