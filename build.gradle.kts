// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.androidx.navigation.safe.args) apply false
}

//buildscript {
//    repositories {
//        google()
//        mavenCentral()
//    }
//    dependencies {
//        // Add the Safe Args plugin classpath here
//        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.5.0")
//    }
//}


