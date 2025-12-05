
fun prop(key: String) = project.findProperty(key) as String
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.chandra.syncnote"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.chandra.syncnote"
        minSdk = 24
        targetSdk = 36
        versionCode = 8
        versionName = "1.1.5"
        //v1.0.0 - Major release (breaking changes)
        //v1.1.0 - Minor release (new features)
        //v1.1.1 - Patch release (bug fixes)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        //noinspection WrongGradleMethod
        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }
   signingConfigs {
       /*create("release") {
           val keystorePath = project.findProperty("STORE_FILE") as String?
               ?: throw GradleException("STORE_FILE not defined in gradle.properties")
           val keystoreFile = file(keystorePath)

           if (!keystoreFile.exists()) {
               throw GradleException("Keystore file not found at: $keystoreFile")
           }

           storeFile = keystoreFile
           storePassword = project.findProperty("STORE_PASSWORD") as String
           keyAlias = project.findProperty("KEY_ALIAS") as String
           keyPassword = project.findProperty("KEY_PASSWORD") as String

           println("Keystore file path: $keystoreFile") // This will print during Gradle sync/build
       }*/
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
       /* getByName("release") {
            isMinifyEnabled = false
            isDebuggable = false
            //signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }*/
        debug {
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
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/*"
        }
    }

    lint {
        abortOnError = false
    }
    configurations.all {
        exclude("androidx.wear.compose")
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Lifecycle Livedata
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")

    // Room
    val room_version = "2.7.0"
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.9.3")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // Hilt
    val dagger_version = "2.56.2"
    implementation("com.google.dagger:hilt-android:$dagger_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_version")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Material Icon Extension
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    // Accompanist - Status Bar
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.36.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

}

kapt {
    correctErrorTypes = true
}