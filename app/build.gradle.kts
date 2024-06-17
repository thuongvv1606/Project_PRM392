plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.restaurantproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.restaurantproject"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    packagingOptions {
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.4.3")
    annotationProcessor("androidx.room:room-compiler:2.4.3")
    androidTestImplementation("androidx.room:room-testing:2.4.3")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.sun.mail:android-mail:1.6.7") // Send mail
    implementation ("com.sun.mail:android-activation:1.6.7") // Send mail
    implementation ("com.google.code.gson:gson:2.8.9") // Session
    implementation ("com.mikhaellopez:circularimageview:4.3.1") //Image
    implementation ("com.github.bumptech.glide:glide:4.11.0") //Image
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0") //Image
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}