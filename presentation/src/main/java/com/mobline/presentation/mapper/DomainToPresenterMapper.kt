package com.mobline.data.mapper

import com.mobline.domain.model.Recipe
import com.mobline.presentation.model.RecipeModel

fun Recipe.toPresenter() = RecipeModel(
    id = id,
    title = title,
    calories = calories,
    description = description,
    thumbnailUrl = thumbnailUrl,
    imageUrl = imageUrl,
    chef = chef,
    tags = tags
)
