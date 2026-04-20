plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.reggya.registeroffline"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.reggya.registeroffline"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        this.buildConfigField ("String", "BASE_URL", "${properties["BASE_URL"]}")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.logging.interceptor)

    //Dagger Hilt
    implementation (libs.hilt.android)

    ksp (libs.hilt.compiler)

    //Pagination
    implementation(libs.androidx.paging.compose)
    implementation (libs.androidx.paging.runtime)

    //room
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //ViewModel
    implementation (libs.androidx.activity.ktx)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)

    //Chucker
    debugImplementation(libs.chucker)
    releaseImplementation(libs.chucker.no.op)

    //M3
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.coil.compose)

    //Datastore
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
}

configurations.all {
    resolutionStrategy {
        force(libs.javapoet)
    }
}