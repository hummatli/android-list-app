package com.mobline.data.mapper

import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.CDAResource
import com.contentful.java.cda.LocalizedResource
import com.contentful.java.cda.image.ImageOption
import com.mobline.domain.model.Recipe

fun CDAResource.toDomain(imageThumbnailWidth: Int? = null): Recipe {
    val thumbnailImageOptionsList = ArrayList<ImageOption>()
    thumbnailImageOptionsList.add(ImageOption.https())
    if (imageThumbnailWidth != null) {
        thumbnailImageOptionsList.add(ImageOption.widthOf(imageThumbnailWidth))
    }
    return Recipe(
        id = id(),
        title = (this as LocalizedResource).getField<String>("title"),
        calories = getField<Double>("calories"),
        description = getField<String>("description"),
        chef = getField<CDAEntry>("chef")?.getField<String>("name"),
        tags = getField<ArrayList<CDAEntry>>("tags")
            ?.map { tag -> tag.getField<String>("name") },
        thumbnailUrl = getField<CDAAsset>("photo")
            ?.urlForImageWith(*thumbnailImageOptionsList.toTypedArray()),
        imageUrl = getField<CDAAsset>("photo")
            ?.urlForImageWith(ImageOption.https())
    )
}
