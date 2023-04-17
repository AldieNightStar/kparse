import java.net.URI

plugins {
    kotlin("jvm") version "1.8.0"
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    
}

group = "haxidenti"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = URI("https://jitpack.io") }
    
}

dependencies {
    
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "haxidenti"
            artifactId = "kparse"
            version = "1.0.0"
            
            from(components["java"])
        }
    }
}