package com.mobline.presentation.di

import com.mobline.presentation.flow.main.MainViewModel
import com.mobline.presentation.flow.main.content.details.DetailsPageViewModel
import com.mobline.presentation.flow.main.content.main.MainPageViewModel
import com.mobline.presentation.flow.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        SplashViewModel()
    }

    viewModel {
        MainPageViewModel(
            fetchRecipesUseCase = get(),
        )
    }

    viewModel {
        MainViewModel()
    }

    viewModel {
        DetailsPageViewModel()
    }
}