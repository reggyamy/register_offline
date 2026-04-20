package com.reggya.registeroffline.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "members")
data class MemberEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // Identity
    val name: String,
    val nik: String,
    val phone: String,
    val birthPlace: String,
    val birthDate: String,
    val status: String,
    val occupation: String,

    // KTP Address
    val address: String,
    val province: String,
    val city: String,
    val district: String,
    val subDistrict: String,
    val postalCode: String,

    // Domicile Address
    val domicileAddress: String,
    val domicileProvince: String,
    val domicileCity: String,
    val domicileDistrict: String,
    val domicileSubDistrict: String,
    val domicilePostalCode: String,

    // Media
    val ktpFilePath: String,
    val ktpFileSecondaryPath: String,
)