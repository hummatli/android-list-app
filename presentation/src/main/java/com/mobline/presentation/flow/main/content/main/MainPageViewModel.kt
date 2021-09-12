package com.mobline.presentation.flow.main.content.main

import com.mobline.data.mapper.toPresenter
import com.mobline.domain.model.Recipe
import com.mobline.domain.usecase.recipe.FetchRecipesUseCase
import com.mobline.presentation.base.BaseViewModel
import com.mobline.presentation.flow.main.content.details.DetailsPageFragment


class MainPageViewModel(
    private val fetchRecipesUseCase: FetchRecipesUseCase
) : BaseViewModel<State, Effect>() {

    init {
        fetchRecipes()
    }

    private fun fetchRecipes() {
        fetchRecipesUseCase.launch(Unit) {
            onSuccess = {
                postState(State.FetchRecipesSuccess(it))
            }

            onError = {
                postState(State.FetchRecipesError)
            }
        }
    }

    fun openDetails(recipe: Recipe) {
        navigate(
            MainPageFragmentDirections.actionMainPageFragmentToDetailsPageFragment(
                recipe.toPresenter()
            ),

        )
    }
}

sealed class Effect {
}

sealed class State {
    class FetchRecipesSuccess(val recipes: List<Recipe>) : State()
    object FetchRecipesError : State()
}
