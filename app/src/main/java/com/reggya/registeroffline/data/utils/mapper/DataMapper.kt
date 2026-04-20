package com.reggya.registeroffline.data.utils.mapper

import com.reggya.registeroffline.data.local.entities.MemberEntity
import com.reggya.registeroffline.data.remote.response.LoginResponse
import com.reggya.registeroffline.data.remote.response.MemberDetailResponse
import com.reggya.registeroffline.data.remote.response.ProfileResponse
import com.reggya.registeroffline.domain.model.Auth
import com.reggya.registeroffline.domain.model.Member
import com.reggya.registeroffline.domain.model.Profile
import com.reggya.registeroffline.domain.model.SyncStatus
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun Member.toEntity(): MemberEntity {
	return MemberEntity(
		id = id,
		name = name,
		nik = nik,
		phone = phone,
		birthPlace = birthPlace,
		birthDate = birthDate,
		status = status,
		occupation = occupation,
		address = address,
		province = province,
		city = city,
		district = district,
		subDistrict = subDistrict,
		postalCode = postalCode,
		domicileAddress = domicileAddress,
		domicileProvince = domicileProvince,
		domicileCity = domicileCity,
		domicileDistrict = domicileDistrict,
		domicileSubDistrict = domicileSubDistrict,
		domicilePostalCode = domicilePostalCode,
		ktpFilePath = ktpFilePath,
		ktpFileSecondaryPath = ktpFileSecondaryPath
	)
}

fun Member.toMultipartMap(): Map<String, RequestBody> {
	return mapOf(
		"name"                      to name.toRequestBody("text/plain".toMediaType()),
		"nik"                       to nik.toRequestBody("text/plain".toMediaType()),
		"phone"                     to phone.toRequestBody("text/plain".toMediaType()),
		"birth_place"               to birthPlace.toRequestBody("text/plain".toMediaType()),
		"birth_date"                to birthDate.toRequestBody("text/plain".toMediaType()),
		"status"                    to status.toRequestBody("text/plain".toMediaType()),
		"occupation"                to occupation.toRequestBody("text/plain".toMediaType()),
		"address"                   to address.toRequestBody("text/plain".toMediaType()),
		"provinsi"                  to province.toRequestBody("text/plain".toMediaType()),
		"kota_kabupaten"            to city.toRequestBody("text/plain".toMediaType()),
		"kecamatan"                 to district.toRequestBody("text/plain".toMediaType()),
		"kelurahan"                 to subDistrict.toRequestBody("text/plain".toMediaType()),
		"kode_pos"                  to postalCode.toRequestBody("text/plain".toMediaType()),
		"alamat_domisili"           to domicileAddress.toRequestBody("text/plain".toMediaType()),
		"provinsi_domisili"         to domicileProvince.toRequestBody("text/plain".toMediaType()),
		"kota_kabupaten_domisili"   to domicileCity.toRequestBody("text/plain".toMediaType()),
		"kecamatan_domisili"        to domicileDistrict.toRequestBody("text/plain".toMediaType()),
		"kelurahan_domisili"        to domicileSubDistrict.toRequestBody("text/plain".toMediaType()),
		"kode_pos_domisili"         to domicilePostalCode.toRequestBody("text/plain".toMediaType()),
	)
}

fun MemberDetailResponse.toDomain(): Member {
	return Member(
		id = id ?: 0,
		name = name ?: "",
		nik = nik ?: "",
		phone = phone ?:"",
		birthPlace = "",
		birthDate = "",
		status = status ?: "",
		occupation = "",
		address = "",
		province = "",
		city = "",
		district = "",
		subDistrict = "",
		postalCode = "",
		domicileAddress = "",
		domicileProvince = "",
		domicileCity = "",
		domicileDistrict = "",
		domicileSubDistrict = "",
		domicilePostalCode = "",
		ktpFilePath = "",
		ktpFileSecondaryPath = "",
		ktpUrl = ktpUrl ?: "",
		ktpUrlSecondary = ktpUrlSecondary ?:"",
		syncStatus = SyncStatus.SYNCED
	)
}

fun MemberEntity.toDomain(): Member {
	return Member(
		id = id,
		name = name,
		nik = nik,
		phone = phone,
		birthPlace = birthPlace,
		birthDate = birthDate,
		status = status,
		occupation = occupation,
		address = address,
		province = province,
		city = city,
		district = district,
		subDistrict = subDistrict,
		postalCode = postalCode,
		domicileAddress = domicileAddress,
		domicileProvince = domicileProvince,
		domicileCity = domicileCity,
		domicileDistrict = domicileDistrict,
		domicileSubDistrict = domicileSubDistrict,
		domicilePostalCode = domicilePostalCode,
		ktpFilePath = ktpFilePath,
		ktpFileSecondaryPath = ktpFileSecondaryPath,
		ktpUrl = "",
		ktpUrlSecondary = "",
		syncStatus = SyncStatus.DRAFT
	)
}

fun List<MemberDetailResponse>.toDomainFromResponse(): List<Member> {
	return map { it.toDomain() }
}

fun List<MemberEntity>.toDomain(): List<Member> {
	return map { it.toDomain() }
}

fun LoginResponse.toDomain(): Auth {
	return Auth(
		token = token ?: ""
	)
}

fun ProfileResponse.toDomain(): Profile {
	return Profile(
		name = full_name ?: "",
		email = email ?: "",
		id = id ?: ""
	)
}
