plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id ("androidx.navigation.safeargs.kotlin") version "2.7.7"
    id ("kotlin-parcelize")
}

android {
    namespace = "com.epf.android_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.epf.android_project"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.retrofit)
    implementation(libs.retrofitGson)

    implementation(libs.okhttp)
    implementation(libs.okhttpLogging)

    implementation(libs.gson)

    implementation(libs.glide)
    annotationProcessor(libs.glideCompiler)

    implementation(libs.zxing)

    implementation(libs.lifecycleViewModel)
    implementation(libs.lifecycleLiveData)


    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.coroutines)
    implementation(libs.zxingAndroidEmbedded)

    implementation("org.jetbrains.kotlin:kotlin-parcelize-runtime:2.0.21")


}
