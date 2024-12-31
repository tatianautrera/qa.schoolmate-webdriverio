// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.navigationArgs) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.firebase) apply false
    alias(libs.plugins.firebase.crash) apply false
}