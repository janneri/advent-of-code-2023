plugins {
    kotlin("jvm") version "1.8.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

tasks {
    wrapper {
        gradleVersion = "7.6"
    }
    test {
        useJUnitPlatform()
    }
}
