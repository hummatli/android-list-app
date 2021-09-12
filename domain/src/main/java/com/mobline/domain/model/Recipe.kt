package com.mobline.domain.model

data class Recipe(
    val id: String? = null,
    val title: String? = null,
    val calories: Double? = null,
    val description: String? = null,
    val thumbnailUrl: String? = null,
    val imageUrl: String? = null,
    val chef: String? = null,
    val tags: List<String>? = null
)