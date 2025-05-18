plugins {
    kotlin("jvm") version "1.9.22"
    `maven-publish`
    `java-library`
    signing
}

group = "ru.iuturakulov"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    // Core dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.assertj:assertj-core:3.25.1")

    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

java {
    withSourcesJar()
    withJavadocJar()
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

// Signing configuration
signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/IslombekTurakulov/tartib-dsl")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            groupId = "ru.iuturakulov"
            artifactId = "tartib-dsl"
            version = "0.1.0"
            
            pom {
                name.set("Tartib DSL")
                description.set("A powerful Kotlin library for defining and managing business rules and validations")
                url.set("https://github.com/IslombekTurakulov/tartib-dsl")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("IslombekTurakulov")
                        name.set("Islombek Turakulov")
                        email.set("islombek.turakulov@gmail.com")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/IslombekTurakulov/tartib-dsl.git")
                    developerConnection.set("scm:git:ssh://github.com/IslombekTurakulov/tartib-dsl.git")
                    url.set("https://github.com/IslombekTurakulov/tartib-dsl")
                }
            }
        }
    }
}