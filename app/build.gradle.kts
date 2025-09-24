


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.example.aiagent"
    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.aiagent"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        
    }

    buildTypes {

        release {
            buildConfigField("String","LM_STUDIO_BASE_URL","\"${project.findProperty("LM_STUDIO_BASE_URL") ?: "http://10.0.2.2:8800/"}\"")
            buildConfigField("String","LM_STUDIO_MODEL","\"${project.findProperty("LM_STUDIO_MODEL") ?: ""}\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String","LM_STUDIO_BASE_URL","\"${project.findProperty("LM_STUDIO_BASE_URL") ?: "http://10.0.2.2:8800/"}\"")
            buildConfigField("String","LM_STUDIO_MODEL","\"${project.findProperty("LM_STUDIO_MODEL") ?: "s3nh/whiterabbitneo-WhiteRabbitNeo-13B-GGUF/whiterabbitneo-WhiteRabbitNeo-13B.Q4_K_S.gguf"}\"")

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

}

dependencies {

    // Network
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.okhttp.dnsoverhttps)

    // DAGGER:HILT
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)


    // Moshi + Kotlin
    implementation(libs.moshi.kotlin)
    //kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.2")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}