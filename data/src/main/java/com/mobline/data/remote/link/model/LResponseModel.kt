package com.mobline.data.remote.link.model

import kotlinx.serialization.Serializable

@Serializable
class LResponseModel(
    val ok: Boolean,
    val result: LModel,
)