plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.yourname.weather"
    //noinspection GradleDependency
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yourname.weather"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // Jetpack Compose BoM
    //noinspection GradleDependency
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    //noinspection GradleDependency
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    //noinspection GradleDependency
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Supabase SDK & Ktor
    //noinspection GradleDependency
    val supabasePostgrest = "io.github.jan-tennert.supabase:postgrest-kt:2.0.0"
    //noinspection GradleDependency
    val supabaseGotrue = "io.github.jan-tennert.supabase:gotrue-kt:2.0.0"
    //noinspection GradleDependency
    val ktorClientAndroid = "io.ktor:ktor-client-android:2.3.7"
    //noinspection GradleDependency
    val ktorClientContentNegotiation = "io.ktor:ktor-client-content-negotiation:2.3.7"
    //noinspection GradleDependency
    val ktorSerialization = "io.ktor:ktor-serialization-kotlinx-json:2.3.7"

    implementation(supabasePostgrest)
    implementation(supabaseGotrue)
    implementation(ktorClientAndroid)
    implementation(ktorClientContentNegotiation)
    implementation(ktorSerialization)
}