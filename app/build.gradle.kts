plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    id("androidx.room")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")

    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
    id("com.google.dagger.hilt.android") version "2.50" apply true

}

android {
    namespace = "com.app.citypulse"
    compileSdk = 35
   room {
        schemaDirectory("$projectDir/schemas")
    }
    defaultConfig {
        applicationId = "com.app.citypulse"
        minSdk = 28
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation( "com.google.maps.android:android-maps-utils:3.5.0")

    //Imagenes desde el movil:
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation(platform("androidx.compose:compose-bom:2025.01.01"))
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.location)


    val room_version = "2.6.1"
    ksp("androidx.room:room-compiler:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    testImplementation("androidx.room:room-testing:$room_version")
    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:4.4.1")

    // Navegación
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    //FireBase:
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-analytics")

    //Crashlitics
    implementation("com.google.firebase:firebase-crashlytics")

    // Firebase Auth y Database
    implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
    implementation("com.google.firebase:firebase-database-ktx:20.0.0")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging:23.0.0")
    implementation(libs.androidx.appcompat)

    implementation ("com.google.android.gms:play-services-auth:20.7.0") // Google Sign-In
    implementation ("com.google.firebase:firebase-auth-ktx:22.1.2") // Firebase Auth
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0") // Corutinas para servicios de Google

    implementation("androidx.compose.material:material-icons-extended:1.6.0")


    // Dependencias de UI y Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")


    // Firebase Auth y Database
    implementation("com.google.firebase:firebase-auth-ktx:23.2.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")

    // Firebase SDK
    implementation("com.google.firebase:firebase-analytics:22.2.0")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging:24.1.0")
    implementation(libs.androidx.appcompat)
    implementation(libs.annotations)

    // Dependencias de prueba
    testImplementation(libs.junit)
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.collection:collection-ktx:1.4.5")


    //Necesario para usuario firebase
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.1.2")

    //Dependencia para las fotos
    implementation("com.google.firebase:firebase-appcheck-playintegrity")

    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
