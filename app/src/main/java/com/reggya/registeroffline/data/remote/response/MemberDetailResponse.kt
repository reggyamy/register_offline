package com.reggya.registeroffline.data.remote.response

import com.google.gson.annotations.SerializedName

data class MemberDetailResponse(
    val id: Long? = null,
    val name: String? = null,
    val email: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val nik: String? = null,
    val phone: String? = null,
    @SerializedName("ktp_url")
    val ktpUrl: String? = null,
    @SerializedName("ktp_url_secondary")
    val ktpUrlSecondary: String? = null
)
