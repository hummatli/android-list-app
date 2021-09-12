package com.mobline.presentation.flow.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mobline.presentation.base.BaseFragment
import com.mobline.marleyspoon.presentation.databinding.FragmentSplashBinding
import kotlin.reflect.KClass

class SplashFragment : BaseFragment<Nothing, Unit, SplashViewModel, FragmentSplashBinding>() {

    override val vmClazz: KClass<SplashViewModel>
        get() = SplashViewModel::class
    override val bindingCallback: (LayoutInflater, ViewGroup?, Boolean) -> FragmentSplashBinding
        get() = FragmentSplashBinding::inflate
}