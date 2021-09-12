package com.mobline.data.remote.auth.error

import kotlinx.serialization.Serializable

@Serializable
class ServerProblemDescription(val error_code: String = "", val error: String = "")