package com.reggya.registeroffline.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    var id: String? = null,
    var full_name: String? = null,
    var email: String? = null,
)