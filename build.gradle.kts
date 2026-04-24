plugins {
    java
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "org.example"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // ---- SPRING BASE ----
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // ---- KAFKA ----
    implementation("org.springframework.kafka:spring-kafka")

    // ---- JSON ----
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")


    // ---- TESTS ----
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

val copyRuntimeLibs by tasks.registering(Copy::class) {
    group = "build"
    description = "Copies runtime dependencies for Docker images"
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("runtime-libs"))
}

// -------------------------------------------
//  IMPORTANT : désactive bootJar car monorepo
// -------------------------------------------
tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

// Active le jar classique
tasks.named<Jar>("jar") {
    enabled = true
}

// Désactive la détection automatique de main
tasks.named("resolveMainClassName") {
    enabled = false
}

// -------------------------------------------
//  Tâche générique runService (optionnelle)
// -------------------------------------------
tasks.register<JavaExec>("runService") {
    group = "application"
    description = "Run a microservice by specifying main class with -PmainClass"
    mainClass.set(findProperty("mainClass")?.toString() ?: "")
    classpath = sourceSets["main"].runtimeClasspath
}

// -------------------------------------------
//  TESTS
// -------------------------------------------
tasks.test {
    useJUnitPlatform()
}

// -------------------------------------------
//  SOURCE SETS
// -------------------------------------------
sourceSets {
    val main by getting {
        java.srcDirs("src/main/java")
        resources.srcDirs("src/main/resources")
    }
    val test by getting {
        java.srcDirs("src/test/java")
        resources.srcDirs("src/test/resources")
    }
}

// -------------------------------------------
//  COPIES
// -------------------------------------------
tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
