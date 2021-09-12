package com.mobline.data.remote.link

import com.mobline.data.remote.link.model.LResponseModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CodeApi {

    @GET("/v2/api")
    suspend fun apicall(
        @Query("url") url: String
    ): LResponseModel
}