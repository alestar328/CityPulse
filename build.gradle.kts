// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false


    //Firebase
    id("com.google.gms.google-services") version "4.4.2" apply false

    //Crashlytics Gradle
    id("com.google.firebase.crashlytics") version "3.0.2" apply false

    //KSP para room, esto va primero
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false

    //Esto es para almacenar en local (1)
    val room_version = "2.6.1"
    id("androidx.room") version "$room_version" apply false


}