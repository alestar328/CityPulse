plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)

    id("androidx.room")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")

    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.app.citypulse"
    compileSdk = 35
   room {
        schemaDirectory("$projectDir/schemas")
    }
    defaultConfig {
        applicationId = "com.app.citypulse"
        minSdk = 27
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

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose:4.4.1")

    // Navegación
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    //FireBase:
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
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


    // Dependencias de UI y Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Firebase Firestore
    implementation(libs.google.firebase.firestore.ktx.v2400)

    // Firebase Auth y Database
    implementation("com.google.firebase:firebase-auth-ktx:23.2.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")

    // Firebase SDK
    implementation("com.google.firebase:firebase-analytics:22.2.0")

    // Firebase Cloud Messaging
    implementation("com.google.firebase:firebase-messaging:24.1.0")
    implementation(libs.androidx.appcompat)

    // Dependencias de prueba
    testImplementation(libs.junit)
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.collection:collection-ktx:1.4.5")


    //Necesario para usuario firebase
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("com.google.firebase:firebase-auth-ktx:23.2.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:25.1.2")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
