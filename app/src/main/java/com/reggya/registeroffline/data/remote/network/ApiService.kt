package com.reggya.registeroffline.data.remote.network

import com.reggya.registeroffline.data.remote.response.ProfileResponse
import com.reggya.registeroffline.data.remote.response.LoginResponse
import com.reggya.registeroffline.data.remote.request.LoginRequest
import com.reggya.registeroffline.data.remote.response.MemberDetailResponse
import com.reggya.registeroffline.data.remote.response.MemberResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiService {
	
	@POST("login")
	suspend fun login(
		@Body loginRequest: LoginRequest
	): Response<LoginResponse>
	
	@GET("profile")
	suspend fun getProfile(): Response<ProfileResponse>

	@Multipart
	@POST("member")
	suspend fun uploadMember(
		@PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
		@Part ktp_file: MultipartBody.Part,
		@Part ktp_file_secondary: MultipartBody.Part
	): Response<MemberResponse>

	@GET("member")
	suspend fun getListMember(): Response<List<MemberDetailResponse>>

}