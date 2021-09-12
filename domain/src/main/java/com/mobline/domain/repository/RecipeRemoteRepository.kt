package com.mobline.domain.repository

import com.mobline.domain.model.Recipe

interface RecipeRemoteRepository {
    suspend fun fetchRecipes(): List<Recipe>
}