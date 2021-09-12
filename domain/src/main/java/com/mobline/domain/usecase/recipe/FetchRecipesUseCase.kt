package com.mobline.domain.usecase.recipe

import com.mobline.domain.base.BaseUseCase
import com.mobline.domain.exceptions.ErrorConverter
import com.mobline.domain.model.Recipe
import com.mobline.domain.repository.RecipeRemoteRepository
import kotlin.coroutines.CoroutineContext

class FetchRecipesUseCase(
    context: CoroutineContext,
    converter: ErrorConverter,
    private val repository: RecipeRemoteRepository,
) : BaseUseCase<Unit, List<Recipe>>(context, converter) {

    override suspend fun executeOnBackground(params: Unit): List<Recipe> {
        return repository.fetchRecipes()
    }
}