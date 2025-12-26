plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.dokka") version "2.1.0"
    id("com.vanniktech.maven.publish") version "0.35.0"
}

group = "io.github.obimp"
version = "0.1.8"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation("io.github.obimp:obimp4j-core:0.1.9")
    implementation("org.bouncycastle:bctls-jdk18on:1.83")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/obimp/obimp4j-client")
            credentials(PasswordCredentials::class)
        }
    }
}

mavenPublishing {
    coordinates(group as String, name as String, version as String)
}