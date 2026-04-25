
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.0-1.0.22"
}

android {
    namespace = "com.example.iyengaryoga20"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.iyengaryoga20"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

    // Включаем поддержку Java 8+ для старых телефонов (Desugaring)
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")


    implementation("androidx.core:core-ktx:1.15.0")

    // Lifecycle (Жизненный цикл)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Activity Compose (Стабильная версия 1.9.3 вместо 1.12.0)
    implementation("androidx.activity:activity-compose:1.9.3")

    // Compose BOM (Управление версиями UI)
    implementation(platform("androidx.compose:compose-bom:2024.12.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Навигация (Стабильная версия)
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Иконки
    implementation("androidx.compose.material:material-icons-extended:1.7.6")

    // Сохранение настроек (DataStore)
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Иконки 
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp (логирование запросов, чтобы видеть ошибки)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Тесты
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.12.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    // Загрузка картинок (Coil)
    implementation("io.coil-kt:coil-compose:2.6.0")

}