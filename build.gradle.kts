plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "cl.duocuc.miregistro"
    compileSdk = 35

    val servidor = providers.gradleProperty("servidorHost").orElse("10.0.2.2").get()
    require(servidor.matches(Regex("[A-Za-z0-9.-]+"))) { "servidorHost debe ser una IP o nombre de host" }

    defaultConfig {
        buildConfigField("String", "SERVIDOR_HOST", "\"$servidor\"")
        applicationId = "cl.duocuc.miregistro"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // La clave de firma se proporciona por variable de entorno, no en el codigo.
    val claveFirma = providers.environmentVariable("INDUZTEK_SIGNING_PASSWORD").orNull
    signingConfigs {
        if (claveFirma != null) {
            create("induztekRelease") {
                storeFile = rootProject.file("firma/induztek.jks")
                storePassword = claveFirma
                keyAlias = "induztek"
                keyPassword = claveFirma
            }
        }
    }
    buildTypes {
        release {
            if (claveFirma != null) signingConfig = signingConfigs.getByName("induztekRelease")
            isMinifyEnabled = false
        }
    }
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
        buildConfig = true
    }
}

dependencies {
    implementation(enforcedPlatform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(enforcedPlatform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    implementation("androidx.navigation:navigation-compose:2.8.5")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
}
