plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.carrentalapp.mvvm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.carrentalapp.mvvm"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    // navigation
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")
    // Gson
    implementation("com.google.code.gson:gson:2.10.1")
    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    // Dagger2
    implementation ("com.google.dagger:dagger:2.35.1")
    implementation ("com.google.dagger:dagger-android:2.35.1")
    implementation ("com.google.dagger:dagger-android-support:2.26")
//    kapt ("com.google.dagger:dagger-compiler:2.31.2")
//    kapt ("com.google.dagger:dagger-android-processor:2.26")

    // okhttp3
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    // swipe refresh layout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    //google
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.android.libraries.places:places:3.4.0")
}