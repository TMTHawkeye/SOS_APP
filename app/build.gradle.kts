plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.raja.myfyp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.raja.myfyp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.core:core-ktx:+")

     implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.firebase:firebase-database-ktx:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.intuit.ssp:ssp-android:1.1.0")
    implementation("com.intuit.sdp:sdp-android:1.1.0")

    implementation ("com.google.android.gms:play-services-location:18.0.0")


    /*Live Data*/
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    /* ViewModel Dependency*/
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    /*Koin*/
    implementation ("io.insert-koin:koin-android:3.5.0")

    implementation ("com.googlecode.libphonenumber:libphonenumber:8.12.34")

    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("io.github.pilgr:paperdb:2.7.2")

    implementation ("com.hbb20:ccp:2.7.0")

    implementation ("com.airbnb.android:lottie:6.4.0")

    implementation ("de.hdodenhof:circleimageview:3.1.0")


    implementation ("org.osmdroid:osmdroid-android:6.1.11")
    implementation ("org.osmdroid:osmdroid-mapsforge:6.1.11")

    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))

    implementation ("com.zeugmasolutions.localehelper:locale-helper-android:1.5.1")



}