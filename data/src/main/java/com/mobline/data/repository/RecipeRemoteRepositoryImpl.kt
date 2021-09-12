package com.mobline.data.repository

import com.contentful.java.cda.CDAClient
import com.contentful.java.cda.CDAEntry
import com.mobline.data.mapper.toDomain
import com.mobline.domain.model.Recipe
import com.mobline.domain.repository.RecipeRemoteRepository

class RecipeRemoteRepositoryImpl(
    private val client: CDAClient
) : RecipeRemoteRepository {

    override suspend fun fetchRecipes(): List<Recipe> {
        val cdaArray = client
            .fetch<CDAEntry>(CDAEntry::class.java)
            ?.withContentType("recipe")
            ?.orderBy("sys.createdAt")
            ?.include(2)
            ?.all()

        return cdaArray?.items()?.map {
            it.toDomain()
        } ?: listOf()
    }
}