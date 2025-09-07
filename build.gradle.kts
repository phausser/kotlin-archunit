plugins {
    kotlin("jvm") version "2.2.10"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation(kotlin("test"))
    testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")
}

tasks.test {
    useJUnitPlatform()
}
